package io.crismp.foxGame.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;

public class Hud implements Disposable{
    public Stage stage;
    private Viewport viewport;

    private Integer life;
    
    Label lblLife;
    Label lblFase;

    public Hud(SpriteBatch sb){
        life=2;

        viewport = new FitViewport(FoxGame.V_WIDTH,FoxGame.V_HEIGHT,new OrthographicCamera());
        stage = new Stage(viewport,sb);

        Table table =new Table();
        table.top();
        table.setFillParent(true);

        lblLife = new Label("Vidas: "+life,new Label.LabelStyle(new BitmapFont(),Color.WHITE));
        lblFase = new Label("Fase 1",new Label.LabelStyle(new BitmapFont(),Color.WHITE));

        table.add(lblLife).expandX().padTop(5);
        table.add(lblFase).expandX().padTop(5);

        stage.addActor(table);
    }

	@Override
	public void dispose() {
		stage.dispose();
	}

}
