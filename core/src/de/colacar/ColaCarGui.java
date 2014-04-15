package de.colacar;

import org.openspaces.core.GigaSpace;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import de.colacar.car.CarPosition;
import de.colacar.roxel.Roxel;
import de.colacar.util.Constants;
import de.colacar.util.Direction;
import de.colacar.util.GigaSpaceConnector;

public class ColaCarGui extends ApplicationAdapter {

	// Gigaspace objects
	private GigaSpace gs;

	// 3D scene objects
	private AssetManager assets;
	private PerspectiveCamera cam;
	private CameraInputController camController;
	private ModelBatch modelBatch;
	private ModelInstance skyInstance;
	private ModelInstance carInstance;
	private Array<ModelInstance> roadInstances = new Array<ModelInstance>();
	private ModelBuilder modelBuilder;
	private Environment environment;
	private boolean loading;

	@Override
	public void create() {

		// Get space
		gs = GigaSpaceConnector.getGigaSpace();

		// Initialize model builder and batcher
		modelBuilder = new ModelBuilder();
		modelBatch = new ModelBatch();

		// Set environment
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f,
				0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f,
				-0.8f, -0.2f));

		// Configure camera
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		cam.position.set(0f, 10f, -10f);
		cam.lookAt(0, 0, 0);
		cam.near = 0.1f;
		cam.far = 300f;
		cam.update();
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		// Initialize asset manager and load models
		assets = new AssetManager();
		assets.load("data/models/car.obj", Model.class);
		assets.load("data/models/sky.obj", Model.class);

		// Start loading
		loading = true;
		
		// Get gigaspace informations in an own thread
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						Gdx.app.log("GigaSpaceThread", "i come in peace");
					}
				});
			}
		}).start();
	}

	private void doneLoading() {

		// Load car model with texture and initialize a car instance
		Model carModel = assets.get("data/models/car.obj", Model.class);
		final Texture carTexture = new Texture(
				Gdx.files.internal("data/textures/car.png"), Format.RGB565,
				true);
		carTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		carModel.materials.get(0).set(
				TextureAttribute.createDiffuse(carTexture));
		carInstance = new ModelInstance(carModel);

		// Load sky model with texture
		Model skyModel = assets.get("data/models/sky.obj", Model.class);
		final Texture skyTexture = new Texture(
				Gdx.files.internal("data/textures/sky.jpg"), Format.RGB565,
				true);
		skyTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);
		skyModel.materials.get(0).set(
				TextureAttribute.createDiffuse(skyTexture));
		skyInstance = new ModelInstance(skyModel);
		skyInstance.transform.scale(100f, 100f, 100f);

		// Create road model with texture
		Model roadModel = modelBuilder.createBox(1f, 0.1f, 1f, new Material(),
				Usage.Position | Usage.Normal | Usage.TextureCoordinates);
		final Texture straightRoadTexture = new Texture(
				Gdx.files.internal("data/textures/straightRoad.jpg"),
				Format.RGB565, true);
		straightRoadTexture.setFilter(TextureFilter.MipMap,
				TextureFilter.Linear);
		final Texture crossXRoadTexture = new Texture(
				Gdx.files.internal("data/textures/crossXRoad.jpg"),
				Format.RGB565, true);
		crossXRoadTexture.setFilter(TextureFilter.MipMap, TextureFilter.Linear);

		// Create road instances from roxels
		Roxel[] roxels = gs.readMultiple(new Roxel());
		for (Roxel roxel : roxels) {
			roadModel.materials
					.get(0)
					.set(TextureAttribute
							.createDiffuse(roxel.getDirection() == Direction.TODECIDE ? crossXRoadTexture
									: straightRoadTexture));
			ModelInstance roadInstance = new ModelInstance(roadModel);
			roadInstance.transform.translate(
					roxel.getPositionX() - Constants.MAP_SIZE_HALF(), 0f,
					roxel.getPositionY() - Constants.MAP_SIZE_HALF());
			if (roxel.getDirection() == Direction.SOUTH
					|| roxel.getDirection() == Direction.NORTH) {
				roadInstance.transform.rotate(0f, 1f, 0f, 90f);
			}
			roadInstances.add(roadInstance);
		}

		loading = false;
	}

	@Override
	public void render() {

		if (loading && assets.update()) {
			doneLoading();
		}

		camController.update();
		cam.update();

		Gdx.gl.glClearColor(0.1f, 0.8f, 0.3f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Draw sky
		if (skyInstance != null) {
			Vector3 camPosition = cam.position.cpy();
			cam.position.set(0f, 0f, 0f);
			cam.update();
			modelBatch.begin(cam);
			modelBatch.render(skyInstance, environment);
			modelBatch.end();
			cam.position.set(camPosition);
			cam.update();
		}

		// Draw road and cars
		modelBatch.begin(cam);
		modelBatch.render(roadInstances, environment);

		// Draw cars from gigaspace
		if (carInstance != null) {
			CarPosition[] carPositions = gs.readMultiple(new CarPosition());
			for (CarPosition carPosition : carPositions) {
				carInstance.transform.idt();
				carInstance.transform.translate(carPosition.getPositionX()
						- Constants.MAP_SIZE_HALF(), 0.2f,
						carPosition.getPositionY() - Constants.MAP_SIZE_HALF());
				carInstance.transform.rotate(0f, 1f, 0f,
						carPosition.getRotationY());
				carInstance.transform.scale(0.5f, 0.5f, 0.5f);
				modelBatch.render(carInstance, environment);
			}
		}

		modelBatch.end();
	}
}
