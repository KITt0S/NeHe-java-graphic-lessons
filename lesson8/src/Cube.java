import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

/**
 * Created by KITS on 22.05.2017.
 */
public class Cube implements GLEventListener, KeyListener {

    private GLU glu = new GLU();
    private int texture;
    private float zTransl;
    private float rCubeX, rCubeY, rCubeZ;
    private float rXCubeSpeed, rYCubeSpeed, rZCubeSpeed;

    public float getzTransl() {
        return zTransl;
    }

    public void setzTransl(float zTransl) {
        this.zTransl = zTransl;
    }

    public float getrXCubeSpeed() {

        return rXCubeSpeed;
    }

    public void setrXCubeSpeed( float rXCubeSpeed ) {

        this.rXCubeSpeed = rXCubeSpeed;
    }

    public float getrYCubeSpeed() {

        return rYCubeSpeed;
    }


    public void setrYCubeSpeed( float rYCubeSpeed ) {
        this.rYCubeSpeed = rYCubeSpeed;
    }

    public float getrZCubeSpeed() {

        return rZCubeSpeed;
    }

    public void setrZCubeSpeed( float rZCubeSpeed ) {

        this.rZCubeSpeed = rZCubeSpeed;
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();

        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0, 0, 0, 0 );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );

        try{

            String path =  "F:\\evrthJAVA\\JGraphics\\lesson8\\crate.png";
            File f = new File( path );
            Texture t = TextureIO.newTexture(f, true);
            texture = t.getTextureObject();
        } catch ( Exception e ){

            e.printStackTrace();
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0, 0, -6.0f + zTransl );

        gl.glRotatef( rCubeX, 1.0f, 0, 0 );
        gl.glRotatef( rCubeY, 0, 1.0f, 0 );
        gl.glRotatef( rCubeZ, 0, 0, 1.0f );

        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texture );

        gl.glBegin( GL2.GL_QUADS );

        //передняя грань
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, 1 );
        gl.glTexCoord2f( 1, 1);
        gl.glVertex3f( 1, 1, 1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, -1, 1 );

        //задняя грань
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, -1 );

        //левая грань
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, 1, 1 );

        //правая грань
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, 1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, -1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, 1, 1 );

        //верхняя грань
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, 1, 1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, 1, 1 );

        //нижняя грань
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, -1, -1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, 1 );

        gl.glEnd();

        gl.glFlush();

        rCubeX += rXCubeSpeed;
        rCubeY += rYCubeSpeed;
        rCubeZ += rZCubeSpeed;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        final GL2 gl = drawable.getGL().getGL2();

        if( height == 0 ) { height = 1; }

        final float h = ( float ) width / ( float ) height;

        gl.glViewport( 0, 0, width, height );

        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        glu.gluPerspective( 45.0f, h, 0.1f, 100.0f );

        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch ( e.getKeyCode() ){

            case KeyEvent.VK_LEFT: {

                rYCubeSpeed -= 0.1f;
                break;
            }

            case KeyEvent.VK_RIGHT: {

                rYCubeSpeed += 0.1f;
                break;
            }

            case KeyEvent.VK_DOWN: {

                rXCubeSpeed -= 0.1f;
                break;
            }

            case KeyEvent.VK_UP: {

                rXCubeSpeed += 0.1f;
                break;
            }

            case KeyEvent.VK_H: {

                zTransl += 0.1f;
                System.out.println( "Координата Z: " +zTransl );
                break;
            }

            case KeyEvent.VK_G: {

                zTransl -= 0.1f ;
                System.out.println( "Координата Z: " +zTransl );
                break;
            }

            default: {

                break;
            }
        }

        System.out.println( "скорость по оси X: "
                +  String.format( "%.1f", rXCubeSpeed )
                + "; скорость по оси Y: " + String.format
                ( "%.1f", rYCubeSpeed ) +"." );
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
