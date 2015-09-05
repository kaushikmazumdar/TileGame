package in.teamkrishna.tilegame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class TileGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img, cover;
	TextureRegion textureRegion[][];
	TextureRegion trTemp;
	Sprite spriteCover;
	Sprite spriteRegion[][];
	OrthographicCamera camera;
//Sprite spritae;
	boolean bSpriteState[][];

	public static int iViewPortWidth, iViewPortHeight;
	//public static final int iViewPortWidth=0;
	//public static final int iViewPortHeight=0;
	int iTileWidth, iTileHeight;
	int iRow, iCol;
	int iMouseX, iMouseY;
	int iNewWidth, iNewHeight;
	BitmapFont font;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("tile.png");
		cover = new Texture("cover.png");
		spriteCover = new Sprite(cover);
		font = new BitmapFont();
		font.setColor(Color.RED);


		//iViewPortWidth = Gdx.graphics.getWidth(); //img.getWidth();
		//iViewPortHeight = Gdx.graphics.getHeight(); //img.getHeight();
		iViewPortWidth = img.getWidth();
		iViewPortHeight = img.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, iViewPortWidth, iViewPortHeight);
		//camera.setToOrtho(false, iViewPortWidth, iViewPortHeight);

		iRow=2;
		iCol=2;
		iTileWidth = iViewPortWidth/iCol;
		iTileHeight = iViewPortHeight/iRow;
		textureRegion = new TextureRegion[iRow][iCol];
		bSpriteState = new boolean[iRow][iCol];
		spriteRegion = new Sprite[iRow][iCol];

		System.out.println("width: " + Gdx.graphics.getWidth());
		System.out.println("height:" + Gdx.graphics.getHeight());

		for(int row=0;row<iRow;row++)
		{
			for(int col=0;col<iCol;col++)
			{
				//textureRegion[row][col] = new TextureRegion(img,col*iTileWidth,row*iTileHeight,iTileWidth,iTileHeight);
				trTemp = new TextureRegion(img,col*iTileWidth,row*iTileHeight,iTileWidth,iTileHeight);
				spriteRegion[row][col] = new Sprite(trTemp);
				//spriteRegion[row][col].setBounds(col*iTileWidth,row*iTileHeight,iTileWidth,iTileHeight);
				bSpriteState[row][col]=false;

				System.out.println(row + "," + col + "," + trTemp.getRegionX() + "," + trTemp.getRegionY());
			}
		}

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		//batch.setTransformMatrix(camera.view);
		batch.setProjectionMatrix(camera.combined);
		//batch.setProjectionMatrix(camera.projection);
		batch.begin();

		for(int row=0;row<iRow;row++)
		{
			for(int col=0;col<iCol;col++)
			{
				//batch.draw(textureRegion[row][col], col * iTileWidth, (iRow - 1 - row) * iTileHeight);
				spriteRegion[row][col].setPosition(col * iTileWidth, (iRow - 1 - row) * iTileHeight);
				spriteRegion[row][col].draw(batch);

				spriteCover.setPosition(col * iTileWidth, (iRow - 1 - row) * iTileHeight);

				if(bSpriteState[row][col])
					spriteCover.setAlpha(0.0f);
				else
					spriteCover.setAlpha(1.0f);

				spriteCover.draw(batch);
			}
		}

		//spriteRegion[0][0].setPosition(0,0);
		//spriteRegion[0][0].draw(batch);


		//batch.draw(img,0,0);
		String str = "mouse x:" + iMouseX + " | mouse y:" + iMouseY;
		font.draw(batch, str, 5, 50);

		str = "new width:" + iNewWidth + " | " + "new height:" + iNewHeight;
		font.draw(batch,str,5,80);
		batch.end();
	}

	public void resize(int width, int height)
	{
		//camera.update();
		batch.dispose();
		batch = new SpriteBatch();

		iNewWidth=width;
		iNewHeight=height;
	}

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
		System.out.println("x: " + screenX);
		System.out.println("y: " + screenY);

		float fPosX = TransformX(screenX);
		float fPosY = TransformY((screenY));

		for(int row=0; row<iRow; row++)
		{
			for(int col=0; col<iCol; col++)
			{
				Rectangle rt = spriteRegion[row][col].getBoundingRectangle();
				System.out.println("rectangle:" + rt.getX() + "|" + rt.getY() + "|" + rt.getWidth() + "|" + rt.getHeight() +
						"|" + fPosX + "|" + fPosY);
				//if(spriteRegion[row][col].getBoundingRectangle().contains(screenX,screenY))
				if(spriteRegion[row][col].getBoundingRectangle().contains(fPosX,fPosY))
				{
					System.out.println("yeah: " + row +","+col);
					bSpriteState[iRow-1-row][col]=true;
				}
			}
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		//System.out.println("mouse x:" + screenX + " | mouse y:" + screenY );
		iMouseX=screenX;
		iMouseY=screenY;

		return true;
	}

	float TransformX(int xPos)
	{
		return (float)(xPos*iViewPortWidth/iNewWidth);
	}

	float TransformY(int yPos)
	{
		return (float)((float)((yPos)*iViewPortHeight)/(float)iNewHeight);
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
