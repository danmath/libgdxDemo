package com.pennypop.project;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;

/**
 * This is where you screen code will go, any UI should be in here
 * 
 * @author Richard Taylor
 */
public class MainScreen implements Screen {
	
	
	private BitmapFont font;
	
	private Button sfxButton;
	private Button apiButton;
	private Button gameButton;
	private HttpRequest request;
	private OrderedMap<String, Object> parsedWeatherResponse;
	
	//audio file
	private Sound buttonClickSound;
	
	private final Stage stage;
	private final SpriteBatch spriteBatch;
	
	private Game game;
	
	public MainScreen(Game game) {
		this.game = game;
		//start load font from file provided
		font = new BitmapFont(Gdx.files.internal("font.fnt"),Gdx.files.internal("font.png"),false);
		//Label label = new Label("", style);
		//end load font from file provided
		
		//start load sfxbutton texture
		Texture texture = new Texture(Gdx.files.internal("sfxButton.png"));
		TextureRegion textureRegion = new TextureRegion(texture);
		TextureRegionDrawable drawable = new TextureRegionDrawable(textureRegion);
		sfxButton = new Button(drawable);
		sfxButton.setPosition((Gdx.graphics.getWidth()/4)-90, (Gdx.graphics.getHeight()/2)-100);
		//end load sfxbutton texture
		
		//start load apibutton texture
		texture = new Texture(Gdx.files.internal("apiButton.png"));
		textureRegion = new TextureRegion(texture);
		drawable = new TextureRegionDrawable(textureRegion);
		apiButton = new Button(drawable);
		apiButton.setPosition((Gdx.graphics.getWidth()/4)+20, (Gdx.graphics.getHeight()/2)-100);
		//end load apibutton texture
		
		//start load gamebutton texture
		texture = new Texture(Gdx.files.internal("gameButton.png"));
		textureRegion = new TextureRegion(texture);
		drawable = new TextureRegionDrawable(textureRegion);
		gameButton = new Button(drawable);
		gameButton.setPosition((Gdx.graphics.getWidth()/4)+130, (Gdx.graphics.getHeight()/2)-100);
		//end load gamebutton texture
		
		buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("button_click.wav"));
		spriteBatch = new SpriteBatch();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);
		
		request = new HttpRequest(HttpMethods.GET);
        request.setUrl("http://api.openweathermap.org/data/2.5/weather?q=San%20Francisco,US&appid=2e32d2b4b825464ec8c677a49531e9ae");
        
        stage.addActor(sfxButton);
        stage.addActor(apiButton);
        stage.addActor(gameButton);
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		stage.dispose();
		buttonClickSound.dispose();
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
		drawSprites();
		handleInput();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this
	}
	
	private void handleInput() {
		if(Gdx.input.justTouched()) {
			//System.out.println("pressed?: " + Gdx.input.getX() + ", y: " + Gdx.input.getY());
			int screenX = Gdx.input.getX();
			int screenY = Gdx.input.getY();
			if(sfxButton.getX() <= screenX && (sfxButton.getX()+sfxButton.getWidth()) >= screenX
					&& (Gdx.graphics.getHeight()-sfxButton.getY()) >= screenY && ((Gdx.graphics.getHeight()-sfxButton.getY())-sfxButton.getHeight()) <= screenY) {
				playAudioFile();
			}
			else if(apiButton.getX() <= screenX && (apiButton.getX()+apiButton.getWidth()) >= screenX
					&& (Gdx.graphics.getHeight()-apiButton.getY()) >= screenY && ((Gdx.graphics.getHeight()-apiButton.getY())-apiButton.getHeight()) <= screenY) {
				makeAPIRequest();
			}
			else if(gameButton.getX() <= screenX && (gameButton.getX()+gameButton.getWidth()) >= screenX
					&& (Gdx.graphics.getHeight()-gameButton.getY()) >= screenY && ((Gdx.graphics.getHeight()-gameButton.getY())-gameButton.getHeight()) <= screenY) {
				playConnectFour();
			}
		}
	}
	
	private void drawSprites() {
		spriteBatch.begin();
		
		//start draw PennyPop text
		font.setColor(Color.RED);
		font.draw(spriteBatch, "PennyPop", (Gdx.graphics.getWidth()/4), (Gdx.graphics.getHeight()/2)+50);
		//end draw PennyPop text
		
		//start draw weather information
		//current weather
		String text;
		font.setColor(Color.RED);
		if(parsedWeatherResponse != null) {
			text = "Current Weather";
		}
		else {
			text = "Unavailable";
		}
		font.draw(spriteBatch, text, ((7*Gdx.graphics.getWidth())/10)-(font.getBounds(text).width/2), (Gdx.graphics.getHeight()/2)+100);
		//location
		font.setColor(Color.BLUE);
		if(parsedWeatherResponse != null) {
			text = (String)parsedWeatherResponse.get("name");
		}
		else {
			text = "Unavailable";
		}
		font.draw(spriteBatch, text, ((7*Gdx.graphics.getWidth())/10)-(font.getBounds(text).width/2), (Gdx.graphics.getHeight()/2)+50);
		//sky condition
		font.setColor(Color.RED);
		if(parsedWeatherResponse != null) {
			text = (String)((OrderedMap<String, Object>)((Array)parsedWeatherResponse.get("weather")).first()).get("description");
		}
		else {
			text = "Unavailable";
		}
		font.draw(spriteBatch, text, ((7*Gdx.graphics.getWidth())/10)-(font.getBounds(text).width/2), (Gdx.graphics.getHeight()/2));
		//temperature and windspeed
		font.setColor(Color.RED);
		if(parsedWeatherResponse != null) {
			OrderedMap<String, Object> winds = (OrderedMap<String, Object>)parsedWeatherResponse.get("wind");
			OrderedMap<String, Object> main = (OrderedMap<String, Object>)parsedWeatherResponse.get("main");
			text = Math.round(Utility.toFarenheit(Utility.toCelcius((Float)main.get("temp")))) + " degrees, " + ((Float)winds.get("speed")).toString() + "mph wind";
		}
		else {
			text = "Unavailable";
		}
		font.draw(spriteBatch, text, ((7*Gdx.graphics.getWidth())/10)-(font.getBounds(text).width/2), (Gdx.graphics.getHeight()/2)-50);
		//end draw weather information
		
		spriteBatch.end();
	}
	
	private void makeAPIRequest() {
		Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
			
			@Override
			public void handleHttpResponse(HttpResponse httpResponse) {
				parsedWeatherResponse = Utility.HttpResponseReader(httpResponse);
				System.out.println("response: " + parsedWeatherResponse.get("weather"));
			}
			
			@Override
			public void failed(Throwable err) {
				Gdx.app.log("Failed", err.getMessage());
			}
		});
	}
	
	private void playAudioFile() {
		buttonClickSound.play();
	}
	
	private void playConnectFour() {
		hide();
		Screen newScreen = new GameScreen(game, this);
		game.setScreen(newScreen);
		newScreen.show();
	}
}
