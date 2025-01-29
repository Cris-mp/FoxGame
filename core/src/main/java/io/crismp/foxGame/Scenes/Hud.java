package io.crismp.foxGame.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
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
    Array<Image> heartArray;



    public Hud(SpriteBatch sb){

        Texture heart=new Texture("heart.png");
        heartArray= new Array<Image>();
        for (int i = 0; i < 7; i++) {
            heartArray.add(new Image(new TextureRegion(heart, 0, i*11, heart.getWidth(), heart.getHeight()/7)));
        }



        life=5;

        viewport = new FitViewport(FoxGame.V_WIDTH*2,FoxGame.V_HEIGHT*2,new OrthographicCamera());
        stage = new Stage(viewport,sb);

        Table table =new Table();
        table.top();
        table.setFillParent(true);

        lblFase = new Label("Fase 1",new Label.LabelStyle(new BitmapFont(),Color.WHITE));


        table.add(heartArray.get(life)).size(99,25).pad(10);

        table.add(lblFase).expandX().padTop(5);
        stage.addActor(table);
    }

	@Override
	public void dispose() {
		stage.dispose();
	}

}
