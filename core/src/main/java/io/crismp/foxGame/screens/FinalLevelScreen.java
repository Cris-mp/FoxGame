package io.crismp.foxGame.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;

public class FinalLevelScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private BitmapFont font;
   


    private Game game;

      public FinalLevelScreen(Game game, PlayScreen screen){
        this.game = game;
        viewport = new FitViewport(FoxGame.V_WIDTH, FoxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((FoxGame) game).batch);

        font = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        font.getData().setScale(1f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label levelCompletedLbl = new Label("NIVEL COMPLETADO", labelStyle);
        Label lblCherries= new Label(String.format("Cerezas = %d x 25 = %d", screen.getCherriesCollected(),screen.getCherriesCollected()*25), labelStyle);
        Label lblGems= new Label(String.format("Gemas = %d x 100 = %d", screen.getGemsCollected(),screen.getCherriesCollected()*100), labelStyle);
        Label playAgainLbl = new Label("Click to Play Again", labelStyle);

        table.add(levelCompletedLbl).expandX();
        table.row();
        table.add(lblCherries).expandX().padTop(10f);
        table.row();
        table.add(lblGems).expandX().padTop(10f);
        table.row();
        table.add(playAgainLbl).expandX().padTop(10f);

        stage.addActor(table);
    }
 
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((FoxGame) game));
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
