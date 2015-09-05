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
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Rectangle;

public class TileGame extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img, cover;
	TextureRegion textureRegion[][];
	TextureRegion trTemp;
	Sprite spriteRegion[][];
	Sprite coverRegion[][];
	OrthographicCamera camera;
	BitmapFont font, bfGameOver;

	boolean bSpriteState[][];
	boolean bGameOver;
	public static int iViewPortWidth, iViewPortHeight;
	int iTileWidth, iTileHeight;
	int iRow, iCol;
	int iMouseX, iMouseY;
	int iNewWidth, iNewHeight;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("tile.jpg");
		cover = new Texture("cover.jpg");

		FreeTypeFontGenerator ftTmp = new FreeTypeFontGenerator(Gdx.files.internal("amiga.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter ftParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
		ftParam.size = 15;
		font = ftTmp.generateFont(ftParam);
		font.setColor(Color.RED);

		ftParam.size = 75;
		bfGameOver = ftTmp.generateFont(ftParam);
		bfGameOver.setColor(Color.WHITE);

		iViewPortWidth = img.getWidth();
		iViewPortHeight = img.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, iViewPortWidth, iViewPortHeight);

		iRow=2;
		iCol=3;
		iTileWidth = iViewPortWidth/iCol;
		iTileHeight = iViewPortHeight/iRow;
		textureRegion = new TextureRegion[iRow][iCol];
		bSpriteState = new boolean[iRow][iCol];
		spriteRegion = new Sprite[iRow][iCol];
		coverRegion = new Sprite[iRow][iCol];
		bGameOver = false;

		for(int row=0;row<iRow;row++)
		{
			for(int col=0;col<iCol;col++)
			{
				trTemp = new TextureRegion(img,col*iTileWidth,row*iTileHeight,iTileWidth,iTileHeight);
				spriteRegion[row][col] = new Sprite(trTemp);
				trTemp = new TextureRegion(cover,col*iTileWidth,row*iTileHeight,iTileWidth,iTileHeight);
				coverRegion[row][col] = new Sprite(trTemp);
				bSpriteState[row][col]=false;
			}
		}
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		for(int row=0;row<iRow;row++)
		{
			for(int col=0;col<iCol;col++)
			{
				spriteRegion[row][col].setPosition(col * iTileWidth, (iRow - 1 - row) * iTileHeight);
				spriteRegion[row][col].draw(batch);
				coverRegion[row][col].setPosition(col * iTileWidth, (iRow - 1 - row) * iTileHeight);

				if(bSpriteState[row][col])
					coverRegion[row][col].setAlpha(0.0f);
				else
					coverRegion[row][col].setAlpha(1.0f);

				coverRegion[row][col].draw(batch);
			}
		}

		String str = "mouse x:" + iMouseX + " | mouse y:" + iMouseY;
		font.draw(batch, str, 5, 100);

		str = "new width:" + iNewWidth + " | " + "new height:" + iNewHeight;
		font.draw(batch,str,5,150);

		if(bGameOver)
		{
			bfGameOver.draw(batch,"Game Over",iViewPortWidth/2,iViewPortHeight/2);
		}
		batch.end();

		if(checkAll())
		{
			bGameOver = true;
		}
	}

	public void resize(int width, int height)
	{
		batch.dispose();
		batch = new SpriteBatch();

		iNewWidth=width;
		iNewHeight=height;
	}

	public void dispose()
	{
		batch.dispose();
		img.dispose();
		cover.dispose();
		trTemp.getTexture().dispose();

		for(int i=0; i<iRow;i++)
		{
			for(int j=0; j<iCol; j++)
			{
				spriteRegion[i][j].getTexture().dispose();
				coverRegion[i][j].getTexture().dispose();
			}
		}

		font.dispose();
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
		Gdx.app.log("touchDown", "x: " + screenX);
		Gdx.app.log("touchDown","y: " + screenY);

		float fPosX = TransformX(screenX);
		float fPosY = TransformY((screenY));

		for(int row=0; row<iRow; row++)
		{
			for(int col=0; col<iCol; col++)
			{
				if(spriteRegion[row][col].getBoundingRectangle().contains(fPosX,fPosY))
				{
					Gdx.app.log("touchDown", "yeah: " + row + "," + col);
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

	public boolean checkAll()
	{
		if(bGameOver)
			return false;

		boolean tmp=bSpriteState[0][0];
		for(int i=0;i<iRow;i++)
		{
			for(int j=0;j<iCol;j++)
			{
				tmp &= bSpriteState[i][j];
			}
		}
		return tmp;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
