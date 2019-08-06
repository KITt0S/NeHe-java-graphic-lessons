import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Field implements GLEventListener, KeyListener {

    private float rCubeX, rCubeY, rCubeZ;
    private float rCubeSpeedX, rCubeSpeedY, rCubeSpeedZ;
    private float zTransl;
    private GLU glu = new GLU();
    private boolean light;
    private FloatBuffer lightAmbient = FloatBuffer.wrap( new float[] { 0.5f, 0.5f, 0.5f, 1.0f } );
    private FloatBuffer lightDiffuse = FloatBuffer.wrap( new float[] { 1.0f, 1.0f, 1.0f, 1.0f } );
    private FloatBuffer lightPosition = FloatBuffer.wrap( new float[] { 0.0f, 0.0f, 2.0f, 1.0f } );
    private IntBuffer textures = IntBuffer.wrap( new int[ 3 ] );
    private int filter = 0;

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        LoadGLTextures( gl );
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0 );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition );
        gl.glEnable( GL2.GL_LIGHT1 );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    private void LoadGLTextures( GL2 gl ) {

        try {

            File txtr = new File( "glass.png" );
            Texture t = TextureIO.newTexture( txtr, true );
            TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txtr, true, null );

            gl.glGenTextures( 3, textures );

            gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( 0 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(), t.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );

            gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( 1 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(), t.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );

            gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( 2 ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_NEAREST );
            GLU glu = GLU.createGLU( gl );
            glu.gluBuild2DMipmaps( GL2.GL_TEXTURE_2D, 3, t.getWidth(), t.getHeight(), GL2.GL_RGB,
                    GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );

        } catch ( Exception e ) {

            e.printStackTrace();
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -6.0f + zTransl );

        gl.glRotatef( rCubeX, 1.0f, 0f, 0f );
        gl.glRotatef( rCubeY, 0f, 1.0f, 0f );
        gl.glRotatef( rCubeX, 0f, 0f, 1.0f );

        gl.glBindTexture( GL2.GL_TEXTURE_2D, textures.get( filter ) );
        gl.glBegin( GL2.GL_QUADS );

        //передняя грань
        gl.glNormal3f( 0.0f, 0.0f, 1.0f );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, 1 );
        gl.glTexCoord2f( 1, 1);
        gl.glVertex3f( 1, 1, 1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, -1, 1 );

        //задняя грань
        gl.glNormal3f( 0.0f, 0.0f, -1.0f );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, -1 );

        //левая грань
        gl.glNormal3f( -1.0f,0.0f, 0.0f);
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( -1, -1, 1 );
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, -1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( -1, 1, 1 );

        //правая грань
        gl.glNormal3f( 1.0f,0.0f, 0.0f);
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( 1, -1, 1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, -1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( 1, 1, 1 );

        //верхняя грань
        gl.glNormal3f( 0.0f,1.0f, 0.0f);
        gl.glTexCoord2f( 0, 0 );
        gl.glVertex3f( -1, 1, 1 );
        gl.glTexCoord2f( 0, 1 );
        gl.glVertex3f( -1, 1, -1 );
        gl.glTexCoord2f( 1, 1 );
        gl.glVertex3f( 1, 1, -1 );
        gl.glTexCoord2f( 1, 0 );
        gl.glVertex3f( 1, 1, 1 );

        //нижняя грань
        gl.glNormal3f( 0.0f,-1.0f, 0.0f);
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

        rCubeX += rCubeSpeedX;
        rCubeY += rCubeSpeedY;
        rCubeZ += rCubeSpeedZ;

        if( light ) {

            gl.glEnable( GL2.GL_LIGHTING );
        } else {

            gl.glDisable( GL2.GL_LIGHTING );
        }
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        if( height == 0 ) {

            height = 1;
        }

        float h = ( float ) width / ( float ) height;
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

                rCubeSpeedY -= 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_RIGHT: {

                rCubeSpeedY += 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;

            }

            case KeyEvent.VK_DOWN: {

                rCubeSpeedX -= 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_UP: {

                rCubeSpeedX += 0.1f;
                System.out.println( "скорость по оси X: " +  String.format( "%.1f", rCubeSpeedX ) +
                        "; скорость по оси Y: " + String.format( "%.1f", rCubeSpeedY ) +"." );
                break;
            }

            case KeyEvent.VK_H: {

                zTransl += 0.1f;
                System.out.println( "Координата Z: " + String.format( "%.1f", zTransl ) );
                break;
            }

            case KeyEvent.VK_G: {

                zTransl -= 0.1f ;
                System.out.println( "Координата Z: " + String.format( "%.1f", zTransl ) );
                break;
            }

            case KeyEvent.VK_F: {

                if( filter != 2 ) {

                    filter ++;
                } else {

                    filter = 0;
                }
                System.out.println( "фильтр: " + "\"" + filter + "\"" );
                break;
            }

            case KeyEvent.VK_L: {

                light = !light;
                break;
            }

            default: {

                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
