package club.greenstudio.minnimafia.states;

import com.badlogic.gdx.Gdx;
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

public class ShopState extends State{

    //Objects
    private Stage stage;
    private final GameStateManager gameStateManager;
    private final PlayState playState;

    //Button back
    private ImageButton btnBack;
    private Drawable drawableBack;
    private Texture textureBtnBack;

    //Label pause
    private ImageButton lShop;
    private Drawable drawableLShop;
    private Texture textureLShop;

    //Background
    private ImageButton background;
    private Drawable drawableBackground;
    private Texture textureBackground;

    public ShopState(GameStateManager gsm, State lastState) {
        super(gsm);
        cam.setToOrtho(false, MinniMafia.WIDTH, MinniMafia.HEIGHT);
        stage = new Stage(new FitViewport(MinniMafia.WIDTH, MinniMafia.HEIGHT));
        gameStateManager = gsm;
        playState = (PlayState) lastState;

        //Label Pause
        textureLShop = new Texture("shopLabel.png");
        drawableBackground = new TextureRegionDrawable(new TextureRegion(textureLShop));
        lShop = new ImageButton(drawableBackground);
        lShop.setPosition((cam.viewportWidth - textureLShop.getWidth())/2, cam.viewportHeight - textureLShop.getHeight() - 50);

        //Button Back
        textureBtnBack = new Texture("backbtn.png");
        drawableBack = new TextureRegionDrawable(new TextureRegion(textureBtnBack));
        btnBack = new ImageButton(drawableBack);
        btnBack.setPosition(10,  20);
        btnBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y)  {
                gameStateManager.set(playState);
            }
        });

        //Background
        textureBackground = new Texture("bgpause.png");
        drawableBackground = new TextureRegionDrawable(new TextureRegion(textureBackground));
        background = new ImageButton(drawableBackground);
        background.setPosition(0,0);

        stage.addActor(background);
        stage.addActor(lShop);
        stage.addActor(btnBack);
        Gdx.input.setInputProcessor(stage);

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
