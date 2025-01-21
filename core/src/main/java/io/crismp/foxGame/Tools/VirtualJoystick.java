package io.crismp.foxGame.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;


public class VirtualJoystick {
	private Stage stage;
	private Texture baseTexture;
	private Table table;
	private Texture knobTexture;
	private Touchpad touchpad;

	public VirtualJoystick(float x, float y, float baseRadius, float knobRadius) {
		stage = new Stage();
		table = new Table();
		table.bottom().left();
		table.pad(15);

		Skin skin = new Skin();
		skin.add("Joystick_base", new Texture("joystick/Joystick.png"));
		skin.add("Joystick_knob", new Texture("joystick/SmallHandleFilledGrey.png"));
		Touchpad.TouchpadStyle touchpadStyle = new TouchpadStyle();
		touchpadStyle.background = skin.getDrawable("Joystick_base");
		touchpadStyle.knob = skin.getDrawable("Joystick_knob");

		touchpad = new Touchpad(10, touchpadStyle);
		touchpad.setSize(20, 20);
		table.add(touchpad);
		stage.addActor(table);

		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean keyDown(int keycode) {
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Vector2 stageCoordinates = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
				table.setPosition(stageCoordinates.x - touchpad.getWidth() / 2,
						stageCoordinates.y - touchpad.getHeight() / 2);
				return true;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				table.setPosition(Gdx.graphics.getWidth() / 2 - touchpad.getWidth(),
						Gdx.graphics.getHeight() / 2 - touchpad.getHeight());
				return true;
			}

			@Override
			public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return false;
			}

			@Override
			public boolean scrolled(float amountX, float amountY) {
				return false;
			}

		});
		Gdx.input.setInputProcessor(stage);

	}

	public Vector2 getDirection() {
		float knobPercentX = touchpad.getKnobPercentX();
		float knobPercentY = touchpad.getKnobPercentY();

		knobPercentY *= 1;

		return new Vector2(knobPercentX, knobPercentY);
	}

	public void render() {
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	public void dispose() {
		baseTexture.dispose();
		knobTexture.dispose();
	}
}

