package club.greenstudio.minnimafia.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import club.greenstudio.minnimafia.MinniMafia;

public class PauseState extends State{

    //Objects
    private Stage stage;
    private final GameStateManager gameStateManager;
    private final PlayState playState;

    //Button continue
    private ImageButton btnContinue;
    private Drawable drawableContinue;
    private Texture textureBtnContinue;

    //Label pause
    private ImageButton lPause;
    private Drawable drawableLPause;
    private Texture textureLPause;

    //Background
    private ImageButton background;
    private Drawable drawableBackground;
    private Texture textureBackground;

    public PauseState( GameStateManager gsm, State lastState){
        super(gsm);
        cam.setToOrtho(false, MinniMafia.WIDTH, MinniMafia.HEIGHT);
        stage = new Stage(new FitViewport(MinniMafia.WIDTH, MinniMafia.HEIGHT));
        gameStateManager = gsm;
        playState = (PlayState) lastState;

        //Button continue
        textureBtnContinue = new Texture("btnContinue.png");
        drawableContinue = new TextureRegionDrawable(new TextureRegion(textureBtnContinue));
        btnContinue = new ImageButton(drawableContinue);
        btnContinue.setPosition(cam.viewportWidth/2 - textureBtnContinue.getWidth()/2,  cam.viewportHeight/2 - textureBtnContinue.getHeight());
        btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                gameStateManager.set(playState);
            }
        });

        //Label Pause
        textureLPause = new Texture("pauseLabel.png");
        drawableLPause = new TextureRegionDrawable(new TextureRegion(textureLPause));
        lPause = new ImageButton(drawableLPause);
        lPause.setPosition((cam.viewportWidth - textureLPause.getWidth())/2, cam.viewportHeight - textureLPause.getHeight() - 50);

        //Background
        textureBackground = new Texture("bgpause.png");
        drawableBackground = new TextureRegionDrawable(new TextureRegion(textureBackground));
        background = new ImageButton(drawableBackground);
        background.setPosition(0,0);

        stage.addActor(background);
        stage.addActor(lPause);
        stage.addActor(btnContinue);
        Gdx.input.setInputProcessor(btnContinue.getStage());
    }
    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        stage.draw();
        sb.end();
    }

    @Override
    public void dispose() {

    }
}
