package io.crismp.foxGame.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;

public class FinalLevelScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private BitmapFont font;

    private Game game;

      public FinalLevelScreen(FoxGame game, PlayScreen screen){
        this.game = game;
        viewport = new FitViewport(FoxGame.V_WIDTH, FoxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((FoxGame) game).batch);
        game.playMusic("audio/music/Victorious.ogg",false);

        font = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        font.getData().setScale(0.6f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Image background = new Image(new TextureRegionDrawable(AssetsManager.getTexture("ui/background.png")));
        background.setFillParent(true); // Hace que ocupe toda la pantalla

        Table table = new Table();
        table.setSize((FoxGame.V_WIDTH/2)+60, (FoxGame.V_HEIGHT/2)+40);
        table.setPosition(FoxGame.V_WIDTH/2-table.getWidth()/2, FoxGame.V_HEIGHT/2-table.getHeight()/2);
        table.background(new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png")));

        Label levelCompletedLbl = new Label("NIVEL COMPLETADO", labelStyle);
        Label lblCherries= new Label(String.format("%d Cerezas x 25 pts : %d", screen.getCherriesCollected(),screen.getCherriesCollected()*25), labelStyle);
        Label lblGems= new Label(String.format("%d Gemas x 100 pts : %d", screen.getGemsCollected(),screen.getGemsCollected()*100), labelStyle);
        Label lblTotal= new Label(String.format("Total: %d pts", (screen.getCherriesCollected()*25)+(screen.getCherriesCollected()*100)), labelStyle);
      

        table.add(levelCompletedLbl);
        table.row();
        table.add(lblCherries).padTop(10f);
        table.row();
        table.add(lblGems).padTop(10f);
        table.row();
        table.add(lblTotal).padTop(10f);

        stage.addActor(background);
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
