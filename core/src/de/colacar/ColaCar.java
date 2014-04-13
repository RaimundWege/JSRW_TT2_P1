package de.colacar;

import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.UrlSpaceConfigurer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.j_spaces.core.client.SQLQuery;

import de.colacar.impl.CarImpl;

public class ColaCar extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		
		String url = "jini://*/*/myGrid";
        System.out.println("Connecting to data grid " + url);
        UrlSpaceConfigurer configurer = new UrlSpaceConfigurer(url);
        GigaSpace gigaSpace = new GigaSpaceConfigurer(configurer).create();

        System.out.println("Write (store) a couple of entries in the data grid:");
        gigaSpace.write(new CarImpl(1, "Vincent", "Chase"));
        gigaSpace.write(new CarImpl(2, "Johnny", "Drama"));

        System.out.println("Read (retrieve) an entry from the grid by its id:");
        CarImpl result1 = gigaSpace.readById(CarImpl.class, 1);
        System.out.println("Result: " + result1);

        System.out.println("Read an entry from the grid using a SQL-like query:");
        CarImpl result2 = gigaSpace.read(new SQLQuery<CarImpl>(CarImpl.class, "firstName=?", "Johnny"));
        System.out.println("Result: " + result2);

        System.out.println("Read all entries of type Person from the grid:");
        CarImpl[] results = gigaSpace.readMultiple(new CarImpl());
        System.out.println("Result: " + java.util.Arrays.toString(results));

        try {
			configurer.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
}
