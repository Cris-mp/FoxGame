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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class VirtualJoystick{
	private Stage stage;
	private Table table;
	// joystick
	private Texture baseTexture;
	private Texture knobTexture;
	private Touchpad touchpad;
	// botones
	boolean jumpPressed;
	public Image jumpImage;

	public VirtualJoystick(float x, float y, float baseRadius, float knobRadius) {

		stage = new Stage();
		table = new Table();
		table.bottom();
		table.setFillParent(true);
		table.pad(20);

		//Joystick
		Skin skin = new Skin();
		skin.add("Joystick_base", new Texture("joystick/Joystick.png"));
		skin.add("Joystick_knob", new Texture("joystick/SmallHandleFilledGrey.png"));
		Touchpad.TouchpadStyle touchpadStyle = new TouchpadStyle();
		touchpadStyle.background = skin.getDrawable("Joystick_base");
		touchpadStyle.knob = skin.getDrawable("Joystick_knob");

		touchpad = new Touchpad(10, touchpadStyle);
		table.add(touchpad);
		Gdx.input.setInputProcessor(stage);

		Image up=new Image(new Texture("joystick/Butto.png"));
		Image down =new Image(new Texture("joystick/Butto-pressed.png"));
		jumpImage = up;
		jumpImage.setSize(20, 20);
		jumpImage.addListener(new ClickListener() {

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				jumpPressed=true;
				jumpImage=down;
                System.out.println("toudown"+jumpPressed);
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				jumpImage=up;
				jumpPressed=false;
                System.out.println("touchup"+jumpPressed);
			}

		});
		table.add().expandX().fillX();
		table.add(jumpImage).size(touchpad.getWidth(),touchpad.getHeight());

		stage.addActor(table);

	}

	public Vector2 getDirection() {
		float knobPercentX = touchpad.getKnobPercentX();
		float knobPercentY = touchpad.getKnobPercentY();

		knobPercentY *= 1;

		return new Vector2(knobPercentX, knobPercentY);
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
		baseTexture.dispose();
		knobTexture.dispose();
	}
}
