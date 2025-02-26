package io.crismp.foxGame.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import io.crismp.foxGame.managers.AssetsManager;

public class VirtualJoystick {

    private final Stage stage;
    private final Table table;
    private final Touchpad touchpad;
    private final Image jumpImage;
    private final Skin skin;
    private final Texture upTexture;
    private final Texture downTexture;

    private boolean jumpPressed;

    public VirtualJoystick(float x, float y, float baseRadius, float knobRadius) {
        // Inicialización de la etapa y la tabla
        stage = new Stage();
        table = new Table();
        table.bottom();
        table.setFillParent(true);
        table.pad(20);

        // Inicialización del skin de la UI
        skin = new Skin();
        skin.add("Joystick_base", AssetsManager.getTexture("joystick/Joystick.png"));
        skin.add("Joystick_knob", AssetsManager.getTexture("joystick/SmallHandleFilledGrey.png"));

        // Inicialización del Joystick
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = skin.getDrawable("Joystick_base");
        touchpadStyle.knob = skin.getDrawable("Joystick_knob");
        touchpad = new Touchpad(10, touchpadStyle);

        // Añadir el Joystick a la tabla
        table.add(touchpad).size(400, 400).pad(10);

        // Inicialización de las texturas de los botones
        upTexture = AssetsManager.getTexture("joystick/boton.png");
        downTexture = AssetsManager.getTexture("joystick/botonPress.png");
        jumpImage = new Image(upTexture);
        jumpImage.setSize(100, 100);
        
        // Listener para el botón de salto
        jumpImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                jumpPressed = true;
                jumpImage.setDrawable(new Image(downTexture).getDrawable());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                jumpPressed = false;
                jumpImage.setDrawable(new Image(upTexture).getDrawable());
            }
        });

        // Añadir el botón de salto a la tabla
        table.add().expandX().fillX();
        table.add(jumpImage).size(touchpad.getWidth(), touchpad.getHeight());

        // Añadir la tabla a la etapa
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Obtiene la dirección del joystick en un Vector2.
     * @return Dirección como un vector normalizado (x, y).
     */
    public Vector2 getDirection() {
        return new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
    }

    /**
     * Establece si el botón de salto está presionado.
     * @param pressed estado de la presión del botón de salto.
     */
    public void setJumpPressed(boolean pressed) {
        this.jumpPressed = pressed;
    }

    /**
     * Verifica si el botón de salto está presionado.
     * @return true si está presionado, false si no.
     */
    public boolean isJumpPressed() {
        return jumpPressed;
    }

    /**
     * Actualiza el renderizado de la interfaz del joystick.
     */
    public void render() {
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Actualiza el tamaño de la vista de la etapa cuando la pantalla cambia de tamaño.
     * @param width nuevo ancho de la pantalla.
     * @param height nuevo alto de la pantalla.
     */
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Libera los recursos utilizados por el joystick.
     */
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
