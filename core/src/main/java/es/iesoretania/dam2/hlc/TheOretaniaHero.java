package es.iesoretania.dam2.hlc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class TheOretaniaHero extends ApplicationAdapter {
    TiledMap map;
    OrthographicCamera camera;
    OrthogonalTiledMapRenderer mapRenderer;
    Texture imagenPersonaje;
    Sprite posicionPersonaje;
    SpriteBatch batch;

    private int mapWidthInPixels;
    private int mapHeightInPixels;
    private float offsetX, offsetY;
    private enum direccion {ARRIBA, ABAJO, IZQUIERDA, DERECHA}

    @Override
    public void create() {
        // Cargar el mapa
        map = new TmxMapLoader().load("tutorial_tiled.tmx");
        // Recoger las propiedades el mapa
        MapProperties properties = map.getProperties();
        int tileWidth = properties.get("tilewidth", Integer.class);
        int tileHeight = properties.get("tileheight", Integer.class);
        int mapWidthInTiles = properties.get("width", Integer.class);
        int mapHeightInTiles = properties.get("height", Integer.class);
        mapWidthInPixels = mapWidthInTiles * tileWidth;
        mapHeightInPixels = mapHeightInTiles * tileHeight;
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        // Crear la cámara
        camera = new OrthographicCamera();
        // Establecer la posición inicial de la cámara en la ventana
        offsetX = 0;
        offsetY = 0;
        // Cargar el Sprite
        batch = new SpriteBatch();
        // Crear la tectura
        imagenPersonaje = new Texture(Gdx.files.internal("UT02_B02_Heroe.png"));
        // Cargar la textura en el Sprite
        posicionPersonaje = new Sprite(imagenPersonaje, 25, 0, 24, 32);
        // Posicionar en el centro
        posicionPersonaje.setPosition((640f - posicionPersonaje.getWidth()) / 2, (480 - posicionPersonaje.getHeight()) / 2);
    }
    @Override
    public void render() {
        String trayectoria = "";
        // Mover personaje

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if(posicionPersonaje.getY() < 0.95 * (camera.viewportHeight - posicionPersonaje.getHeight())){
                posicionPersonaje.translate(0f, 400 * Gdx.graphics.getDeltaTime());
            }else{
                offsetY += 400 * Gdx.graphics.getDeltaTime();
            }
            trayectoria += direccion.ARRIBA.toString();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if(posicionPersonaje.getY() > 0.95 * (camera.viewportHeight - camera.viewportHeight + posicionPersonaje.getHeight())){
                posicionPersonaje.translate(0f, -400 * Gdx.graphics.getDeltaTime());
            }else{
                offsetY -= 400 * Gdx.graphics.getDeltaTime();
            }
            trayectoria += direccion.ABAJO.toString();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if(posicionPersonaje.getX() > 0.95 * (camera.viewportWidth  - camera.viewportWidth + posicionPersonaje.getWidth())){
                posicionPersonaje.translate(-400f * Gdx.graphics.getDeltaTime(), 0);
            }else{
                offsetX -= 400 * Gdx.graphics.getDeltaTime();
            }
            trayectoria += direccion.IZQUIERDA.toString();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if(posicionPersonaje.getX() < 0.95 * (camera.viewportWidth) - posicionPersonaje.getWidth()){
                posicionPersonaje.translate(400f * Gdx.graphics.getDeltaTime(), 0);
            }else{
                offsetX += 400 * Gdx.graphics.getDeltaTime();
            }
            trayectoria += direccion.DERECHA.toString();
        }

        switch(trayectoria){
            case "ARRIBA":
                posicionPersonaje.setRegion(25, 0, 24, 32);
                break;
            case "ABAJO":
                posicionPersonaje.setRegion(25, 66, 24, 32);
                break;
            case "IZQUIERDA":
                posicionPersonaje.setRegion(25, 97, 24, 32);
                break;
            case "DERECHA":
                posicionPersonaje.setRegion(25, 33, 24, 32);
                break;
            case "ARRIBAIZQUIERDA":
                posicionPersonaje.setRegion(25, 0, 24, 32);
                break;
            case "ARRIBADERECHA":
                posicionPersonaje.setRegion(25, 0, 24, 32);
                break;
            case "ABAJOIZQUIERDA":
                posicionPersonaje.setRegion(25, 66, 24, 32);
                break;
            case "ABAJODERECHA":
                posicionPersonaje.setRegion(25, 66, 24, 32);
                break;
        }

        if (offsetX < 0) {
            posicionPersonaje.translate(-400f * Gdx.graphics.getDeltaTime(), 0);
            offsetX = 0;
        }
        if (offsetY > 0) {
            posicionPersonaje.translate(0f, 400 * Gdx.graphics.getDeltaTime());
            offsetY = 0;
        }
        if (offsetX > mapWidthInPixels - camera.viewportWidth) {
            posicionPersonaje.translate(400f * Gdx.graphics.getDeltaTime(), 0);
            offsetX = mapWidthInPixels - camera.viewportWidth;
        }
        if (offsetY < -mapHeightInPixels + camera.viewportHeight) {
            posicionPersonaje.translate(0f, -400 * Gdx.graphics.getDeltaTime());
            offsetY = -mapHeightInPixels + camera.viewportHeight;
        }

        if (posicionPersonaje.getX() < 0) posicionPersonaje.setX(0);
        if (posicionPersonaje.getX() > camera.viewportWidth - posicionPersonaje.getWidth()) posicionPersonaje.setX(camera.viewportWidth - posicionPersonaje.getWidth());
        if (posicionPersonaje.getY() < 0) posicionPersonaje.setY(0);
        if (posicionPersonaje.getY() > camera.viewportHeight - posicionPersonaje.getHeight()) posicionPersonaje.setY(camera.viewportHeight - posicionPersonaje.getHeight());

        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        // Recalcular todas las matrices de la cámara
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();
        batch.begin();
        posicionPersonaje.draw(batch);
        batch.end();
    }
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        camera.position.x = camera.viewportWidth / 2 + offsetX;
        camera.position.y = mapHeightInPixels - camera.viewportHeight / 2 + offsetY;
        camera.update();
    }
    @Override
    public void dispose() {
        map.dispose();
    }
}