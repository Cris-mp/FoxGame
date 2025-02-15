package io.crismp.foxGame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;

public class GameOverScreen implements Screen{
    private Viewport viewport;
    private Stage stage;
    private BitmapFont font;

    private Game game;

      public GameOverScreen(FoxGame game){
        this.game = game;
        viewport = new FitViewport(FoxGame.V_WIDTH, FoxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((FoxGame) game).batch);
        game.playMusic("audio/music/To Suffer a Loss (Game Over).ogg",false);

        font = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        font.getData().setScale(1f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Table table = new Table();
        table.center();
        table.setFillParent(true);
        // Image background = new Image(new TextureRegionDrawable(AssetsManager.getTexture("ui/background.png")));
        // background.setFillParent(true); // Hace que ocupe toda la pantalla

        Label gameOverLabel = new Label(LanguageManager.get("game_over"),labelStyle);
       Image skull = (new Image(new TextureRegion(AssetsManager.getTexture("hud/skull2.png"), 0, 0, 430, 414)));

        table.add(gameOverLabel).expandX();
        table.row();
        table.add(skull).size(100, 100);

        //stage.addActor(background);
        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new MainMenuScreen((FoxGame) game));
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }
    
}
