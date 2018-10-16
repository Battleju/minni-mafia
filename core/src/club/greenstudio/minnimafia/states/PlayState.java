package club.greenstudio.minnimafia.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.geom.Line2D;
import java.security.SecureRandom;
import java.util.Random;

import club.greenstudio.minnimafia.MinniMafia;
import club.greenstudio.minnimafia.entities.Border;
import club.greenstudio.minnimafia.entities.Drug;
import club.greenstudio.minnimafia.entities.EntityManager;
import club.greenstudio.minnimafia.entities.House;
import club.greenstudio.minnimafia.entities.Player;
import club.greenstudio.minnimafia.rts.RoadTravelSystem;
import club.greenstudio.minnimafia.utils.DebugClass;

public class PlayState extends State {

    //Objects
    private final GameStateManager gameStateManager;
    private final State me;
    private EntityManager entityManager;
    private RoadTravelSystem rts;
    private Player player;
    private Texture background;
    private Texture filter, raster;
    private BitmapFont font;
    private Sound click, boost;
    private Drug drug;
    private Border border;
    private House house;


    //Constants
    public static final int FIELD_SIZE_HEIGHT = 1000;
    public static final int FIELD_SIZE_WIDTH = 500;

    //Variables
    private int collectedDrugs;
    private boolean pause, renderLost;
    private boolean boostSound;

    //Stage
    private Stage stage;

    //Button pause
    private ImageButton btnPause;
    private Drawable drawablePause;
    private Texture txtBtnPause;

    //Button shop
    private ImageButton btnShop;
    private Drawable drawableShop;
    private Texture txtBtnShop;

    //label count
    Label label1;

    public PlayState(GameStateManager gsm) {
        super(gsm);

        //Objects
        gameStateManager = gsm;
        me = this;
        entityManager = new EntityManager();
        house = new House();
        rts = new RoadTravelSystem(this, house.getBounds(), true);
        drug = new Drug(generateDrugPosition());
        player = new Player(this, 100, 100, rts.getBetterRouteNavigator());
        border = new Border();
        background = new Texture("bggame.png");
        filter = new Texture("filter.png");
        font = new BitmapFont();
        cam.setToOrtho(false, MinniMafia.WIDTH, MinniMafia.HEIGHT);
        raster = new Texture("raster.png");

        //Variables
        click = Gdx.audio.newSound(Gdx.files.internal("Click.mp3"));
        boost = Gdx.audio.newSound(Gdx.files.internal("boost.mp3"));
        collectedDrugs = 0;
        pause = false;
        renderLost = false;
        boostSound = false;

        //Entity adds
        entityManager.addEntity(player);
        entityManager.addEntity(drug);
        entityManager.addEntity(border);
        entityManager.addEntity(house);


        //UI---------------------------------------------------------------------------------------------------------------------------------------
        stage = new Stage(new FitViewport(MinniMafia.WIDTH, MinniMafia.HEIGHT));

        //Button Pause
        stage = new Stage(new FitViewport(MinniMafia.WIDTH, MinniMafia.HEIGHT));
        txtBtnPause = new Texture("pausebtn.png");
        drawablePause = new TextureRegionDrawable(new TextureRegion(txtBtnPause));
        btnPause = new ImageButton(drawablePause);
        btnPause.setPosition(stage.getWidth() - txtBtnPause.getWidth() - 10, stage.getHeight() - txtBtnPause.getHeight() - 10);
        btnPause.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStateManager.set(new PauseState(gameStateManager, me));
            }
        });
        stage.addActor(btnPause);

        //Button Shop

        txtBtnShop = new Texture("shopbtn.png");
        drawableShop = new TextureRegionDrawable(new TextureRegion(txtBtnShop));
        btnShop = new ImageButton(drawableShop);
        btnShop.setPosition(stage.getWidth() - txtBtnShop.getWidth() * 2 - 20, stage.getHeight() - txtBtnShop.getHeight() - 10);
        btnShop.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameStateManager.set(new ShopState(gameStateManager, me));
            }
        });
        stage.addActor(btnShop);

        //Label drugcount
        Label.LabelStyle label1Style = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont();
        label1Style.font = myFont;
        label1Style.fontColor = Color.WHITE;
        label1 = new Label("", label1Style);
        label1.setPosition(10, 10);
        label1.setAlignment(Align.center);
        stage.addActor(label1);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched()) {
            click.play(0.2f);
        }
        if (Gdx.input.isTouched()) {
            player.setStop(true);
            boostSound = false;
        } else {
            if (!boostSound) {
                boost.play(0.2f);
                boostSound = true;
            }
            player.setStop(false);

        }

    }

    @Override
    public void update(float dt) {
        Gdx.input.setInputProcessor(stage);
        stage.act(dt);
        label1.setText("" + collectedDrugs);
        if (!pause) {
            handleInput();
            entityManager.updateEntities(dt);
            setCam();
            if (drug.collides(player.getBounds())) {
                entityManager.disposeEntity(drug);
                player.setGotDrug(true);
            }
            if (border.collides(player.getBounds()) && !border.isOpen()) {
                pause = true;
                renderLost = true;

            }
            if (house.collides(player.getBounds()) && player.isGotDrug()) {
                drug = new Drug(generateDrugPosition());
                collectedDrugs++;
                player.setGotDrug(false);
                entityManager.addEntity(drug);
            }
        } else if (renderLost == true) {
            if (Gdx.input.isTouched()) {
                gsm.set(new MenuState(gsm));
                dispose();
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.draw(background, 0, 0);
        rts.renderRoad(sb);
        entityManager.renderEntities(sb);
        font.draw(sb, "LOST! SCORE:" + collectedDrugs, cam.position.x - 50, cam.position.y);
        if (renderLost) {
            sb.draw(filter, cam.position.x - cam.viewportWidth / 2, cam.position.y - cam.viewportHeight / 2);
            font.draw(sb, "LOST! SCORE:" + collectedDrugs, cam.position.x - 50, cam.position.y);
        }
        if (false) {
            sb.draw(raster, 0, 0);

        } else {
            stage.draw();
        }

/*
        for (int i = 0; i < rts.getBetterRouteNavigator().getLinesV().size(); i++) {
            DebugClass.DrawDebugLine(rts.getBetterRouteNavigator().getLinesV().get(i).getP1(), rts.getBetterRouteNavigator().getLinesV().get(i).getP2(), 2, Color.BLACK, cam.combined);
            DebugClass.DrawDebugRectangle(rts.getBetterRouteNavigator().getLinesV().get(i).forJunction(), cam.combined, Color.WHITE);
        }
        for (int i = 0; i < rts.getBetterRouteNavigator().getLinesH().size(); i++) {
            DebugClass.DrawDebugLine(rts.getBetterRouteNavigator().getLinesH().get(i).getP1(), rts.getBetterRouteNavigator().getLinesH().get(i).getP2(), 2, Color.BLACK, cam.combined);
            DebugClass.DrawDebugRectangle(rts.getBetterRouteNavigator().getLinesH().get(i).forJunction(), cam.combined, Color.WHITE);
        }

        for (int i = 0; i < rts.getBetterRouteNavigator().getLinesH().size(); i++) {

        }
        for(int c = 0; c < 2; c++){
            for (int i = 0; i < rts.getBetterRouteNavigator().getStart_dsp_group().get(c).getSize(); i++) {
                DebugClass.DrawDebugLine(rts.getBetterRouteNavigator().getStart_dsp_group().get(c).get(i).getP1(), rts.getBetterRouteNavigator().getStart_dsp_group().get(c).get(i).getP2(), 3, Color.BLACK, cam.combined);
            }
        }

        for(int c = 0; c < 2; c++){
            for (int i = 0; i < rts.getBetterRouteNavigator().getAwayRoutes().get(c).getSize(); i++) {
                DebugClass.DrawDebugRectangle(new Rectangle(rts.getBetterRouteNavigator().getAwayRoutes().get(c).getPoint(i).x, rts.getBetterRouteNavigator().getAwayRoutes().get(c).getPoint(i).y, 10,10),cam.combined, Color.WHITE);
            }
        }
*/
        sb.end();
    }

    @Override
    public void dispose() {
        background.dispose();
        entityManager.disposeAll();
        click.dispose();
    }

    private void setCam() {

        float camViewportHalfX = cam.viewportWidth * .5f;
        float camViewportHalfY = cam.viewportHeight * .5f;

        cam.position.x = player.getPosition().x;
        cam.position.y = player.getPosition().y;
        cam.position.x = MathUtils.clamp(cam.position.x, camViewportHalfX, FIELD_SIZE_WIDTH - camViewportHalfX);
        cam.position.y = MathUtils.clamp(cam.position.y, camViewportHalfY, FIELD_SIZE_HEIGHT - camViewportHalfY);

        cam.update();
    }

    private int[] generateDrugPosition() {
        Random random = new SecureRandom();
        int type = (1 + random.nextInt(3) - 1);
        Vector2 position = new Vector2(rts.getDrugSpawnSpots()[type].getPosition());
        position.x += rts.getDrugSpawnSpots()[0].getTexture().getWidth() / 2;
        position.y += rts.getDrugSpawnSpots()[0].getTexture().getHeight() / 2;
        int[] objects = new int[3];
        objects[0] = type;
        objects[1] = Math.round(position.x);
        objects[2] = Math.round(position.y);
        return objects;
    }

    public Drug getDrug() {
        return drug;
    }

    public House getHouse() {
        return house;
    }
}
