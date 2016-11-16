package com.pennypop.project;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;

/**
 * The {@link ApplicationListener} for this project, create(), resize() and
 * render() are the only methods that are relevant
 * 
 * @author Richard Taylor
 * */
public class ProjectApplication extends Game implements ApplicationListener {

	private Screen screen;
	
	//start weather report response
	//city name
	//main,description
	//degrees kelivn->farenheit
	//winds m/s -> mph
	//end weather report response
	
	//audio file
	

	public static void main(String[] args) {
		new LwjglApplication(new ProjectApplication(), "PennyPop", 1280, 720,
				true);
	}

	@Override
	public void create() {
		screen = new MainScreen(this);
		screen.show();
	}

	@Override
	public void dispose() {
		screen.hide();
		screen.dispose();
	}

	@Override
	public void pause() {
		screen.pause();
	}

	@Override
	public void render() {
		clearWhite();
		screen.render(Gdx.graphics.getDeltaTime());
		//super.render();
	}

	/** Clears the screen with a white color */
	private void clearWhite() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
		screen.resize(width, height);
	}

	@Override
	public void resume() {
		screen.resume();
	}
	
	public void setScreen(Screen screen) {
		this.screen = screen;
	}
}
