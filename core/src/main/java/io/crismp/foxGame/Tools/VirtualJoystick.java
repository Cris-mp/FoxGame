package io.crismp.foxGame.Tools;

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
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;

public class VirtualJoystick {
	private Stage stage;
	private Table table;
	// joystick
	private Touchpad touchpad;
	// botones
	private boolean jumpPressed;
	public Image jumpImage;
	private Skin skin;
	private Texture upTexture;
	private Texture downTexture;

	public VirtualJoystick(float x, float y, float baseRadius, float knobRadius) {

		stage = new Stage();
		table = new Table();
		jumpPressed = false;
		table.bottom();
		table.setFillParent(true);
		table.pad(20);

		// Joystick
		skin = new Skin();
		skin.add("Joystick_base", new Texture("joystick/Joystick.png"));
		skin.add("Joystick_knob", new Texture("joystick/SmallHandleFilledGrey.png"));
		Touchpad.TouchpadStyle touchpadStyle = new TouchpadStyle();
		touchpadStyle.background = skin.getDrawable("Joystick_base");
		touchpadStyle.knob = skin.getDrawable("Joystick_knob");

		touchpad = new Touchpad(10, touchpadStyle);
		table.add(touchpad).pad(10);

		upTexture = new Texture("joystick/boton.png");
		downTexture = new Texture("joystick/botonPress.png");
		jumpImage = new Image(upTexture);
		jumpImage.setSize(100, 100);
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
				Gdx.app.postRunnable(() -> jumpImage.setDrawable(new Image(upTexture).getDrawable()));

			}

		});
		table.add().expandX().fillX();
		table.add(jumpImage).size(touchpad.getWidth(), touchpad.getHeight());

		stage.addActor(table);
		Gdx.input.setInputProcessor(stage);
	}

	public Vector2 getDirection() {
		return new Vector2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
	}
	public void setJumpPressed(boolean bool) {
        jumpPressed = bool;
    }

	public boolean isJumpPressed() {
		return jumpPressed;
	}

	public void render() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose() {
		stage.dispose();
		if (skin != null) {
			skin.dispose();
		}
	}
}
