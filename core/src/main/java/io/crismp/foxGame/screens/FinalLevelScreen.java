package io.crismp.foxGame.screens;

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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;
import io.crismp.foxGame.managers.LanguageManager;
import io.crismp.foxGame.tools.GamePreferences;

public class FinalLevelScreen implements Screen {
    private Viewport viewport;
    private Stage stage;
    private BitmapFont font;

    private FoxGame game;
    private int nivelActual;

    public FinalLevelScreen(int nivelActual, FoxGame game, PlayScreen screen) {
        this.game = game;
        this.nivelActual = nivelActual;
        viewport = new FitViewport(FoxGame.V_WIDTH, FoxGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, (game).batch);
        game.playMusic("audio/music/Victorious.ogg", false);

        font = new BitmapFont(Gdx.files.internal("fonts/wood.fnt"));
        font.getData().setScale(0.6f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        Image background = new Image(new TextureRegionDrawable(AssetsManager.getTexture("ui/background.png")));
        background.setFillParent(true); // Hace que ocupe toda la pantalla

        Table table = new Table();
        table.setSize((FoxGame.V_WIDTH / 2) + 60, (FoxGame.V_HEIGHT / 2) + 40);
        table.setPosition(FoxGame.V_WIDTH / 2 - table.getWidth() / 2, FoxGame.V_HEIGHT / 2 - table.getHeight() / 2);
        table.background(new TextureRegionDrawable(AssetsManager.getTexture("ui/backSettings.png")));

        Label levelCompletedLbl = new Label(LanguageManager.get("complete"), labelStyle);
        Label lblCherries = new Label(String.format(LanguageManager.get("cherries"), screen.getCherriesCollected(),
                screen.getCherriesCollected() * 25), labelStyle);
        Label lblGems = new Label(
                String.format(LanguageManager.get("gems"), screen.getGemsCollected(), screen.getGemsCollected() * 100),
                labelStyle);
        int totalScore = ((screen.getCherriesCollected() * 25) + (screen.getGemsCollected() * 100));
        Label lblTotal = new Label(String.format(LanguageManager.get("total_score"),
                totalScore), labelStyle);
        //GamePreferences.saveScore(totalScore);
        GamePreferences.saveScore(totalScore, false);

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
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            switch (nivelActual) {
                case 1:
                game.setScreen(new PlayScreen(game, nivelActual+1));
                    break;
                default:
                GamePreferences.resetAccumulatedScore();
                game.setScreen(new MainMenuScreen(game));
                    break;
            }
            
            dispose();
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    // Métodos no utilizados, pero necesarios por la interfaz Screen
    @Override
    public void show() {
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

}
