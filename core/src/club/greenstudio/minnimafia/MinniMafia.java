package club.greenstudio.minnimafia;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import club.greenstudio.minnimafia.states.GameStateManager;
import club.greenstudio.minnimafia.states.MenuState;

/*
Detected Bugs:
none

Plan:
RTS - RoadTravelSystem - overwite it later
Shop
deliver feature (to earn money)
power ups (to speed up the business)
optimize(dispose methods)
new Menu
Settings
 */
public class MinniMafia extends ApplicationAdapter {

	public static final int WIDTH = 480;
	public static final int HEIGHT = 800;

	public static final String TITLE = "Mini Mafia";

	private GameStateManager gsm;
	private SpriteBatch batch;
	private Music music;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music.setLooping(true);
		music.setVolume(0.1f);
		music.play();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
		music.dispose();
	}
}
