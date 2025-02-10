package io.crismp.foxGame.scenes;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import io.crismp.foxGame.FoxGame;
import io.crismp.foxGame.managers.AssetsManager;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer life;
    private Integer cherries;
    private Integer gems;

    Label lblLife;
    Label lblCherries;
    Label lblGems;
    Table leftTable;

    ArrayList<Image> heartArray;
    Image cherry;
    Image gem;

    private BitmapFont font;

    public Hud(SpriteBatch sb) {

        Texture heart = AssetsManager.getTexture("hud/heart.png");
        heartArray = new ArrayList<Image>();
        for (int i = 6; i >= 0; i--) {
            heartArray.add(new Image(new TextureRegion(heart, 0, i * 11, heart.getWidth(), heart.getHeight() / 7)));
        }
        cherry = (new Image(new TextureRegion(AssetsManager.getTexture("items/cherry.png"), 0, 0, 21, 21)));
        gem = (new Image(new TextureRegion(AssetsManager.getTexture("items/gem.png"), 0, 0, 15, 15)));

        font = AssetsManager.getFont("fonts/wood.fnt");
        font.getData().setScale(1.2f);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        life = 6;
        cherries = 0;
        gems = 0;

        viewport = new FitViewport(FoxGame.V_WIDTH * 2, FoxGame.V_HEIGHT * 2, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        lblCherries = new Label(String.format("x%02d", cherries), labelStyle);
        lblGems = new Label(String.format("x%02d", gems), labelStyle);

        // *** TABLA PRINCIPAL ***
        Table rootTable = new Table();
        rootTable.top();
        rootTable.setFillParent(true);

        // *** TABLA IZQUIERDA (VIDAS) ***
        leftTable = new Table();
        leftTable.add(heartArray.get(life)).size(99, 25).pad(5).left();

        // *** TABLA DERECHA (GEMAS Y CEREZAS) ***
        Table rightTable = new Table();
        rightTable.top().right().padTop(5);
        rightTable.add(lblCherries).right().padRight(10);
        rightTable.add(cherry).size(25, 25).padRight(10).row();
        rightTable.add(lblGems).right().padRight(10);
        rightTable.add(gem).size(25, 25).padRight(10);

        // Añadir tablas a la tabla principal
        rootTable.add(leftTable).expandX().left();
        rootTable.add().expandX();
        rootTable.add(rightTable).expandX().right();

        stage.addActor(rootTable);
    }

    public void updateHud(int newLife, int newCherries, int newGems) {
       this.life=newLife;
        lblCherries.setText(String.format("x%02d", newCherries));
        lblGems.setText(String.format("x%02d", newGems));
        if (newLife >= 0 && newLife < heartArray.size()) {
            leftTable.clearChildren(); // Borra los elementos previos de la tabla izquierda
            leftTable.add(heartArray.get(life)).size(99, 25).pad(5).left(); // Añade el nuevo corazón
        }
    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
    }

}
