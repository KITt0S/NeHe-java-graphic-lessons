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
import java.io.IOException;
import java.nio.FloatBuffer;

public class Field implements GLEventListener, KeyListener {

    private float rotX, rotY;
    private float xSpeed, ySpeed;
    private int textureID;
    private GLU glu;
    private int[] fogMode = new int[]{ GL2.GL_EXP, GL2.GL_EXP2, GL2.GL_LINEAR };
    private int fogFilter = 0;
    private FloatBuffer fogColor = FloatBuffer.wrap( new float[]{ 0.5f, 0.5f, 0.5f, 1f } );
    private FloatBuffer lightAmbient = FloatBuffer.wrap( new float[]{ 0.5f, 0.5f, 0.5f, 1.f } );
    private FloatBuffer lightDiffuse = FloatBuffer.wrap( new float[]{ 1f, 1f, 1f, 1f } );
    private FloatBuffer lightPosition = FloatBuffer.wrap( new float[]{ 0f, 0f, 2f, 1f } );
    private boolean isLighting;



    private void loadGLTexture( GL2 gl ) throws IOException {

        File txFile = new File( "Data/crate.png" );
        Texture t = TextureIO.newTexture( txFile, true );
        textureID = t.getTextureObject();
        TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txFile, true, null );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureID );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
        gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
        gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(), t.getHeight(), 0,
                GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        try {

            loadGLTexture( gl );
        } catch ( IOException e ) {

            e.printStackTrace();
        }
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0.5f, 0.5f, 0.5f, 1f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        glu = GLU.createGLU( gl );
        gl.glEnable( GL2.GL_FOG );
        gl.glFogi( GL2.GL_FOG_MODE, fogMode[ fogFilter ] );
        gl.glFogfv( GL2.GL_FOG_COLOR, fogColor );
        gl.glFogf( GL2.GL_FOG_DENSITY, 0.35f );
        gl.glHint( GL2.GL_FOG_HINT, GL2.GL_DONT_CARE );
        gl.glFogf( GL2.GL_FOG_START, 1f );
        gl.glFogf( GL2.GL_FOG_END, 5f );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse );
        gl.glLightfv( GL2.GL_LIGHT1, GL2.GL_POSITION, lightPosition );
        gl.glEnable( GL2.GL_LIGHT1 );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {


    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glFogi( GL2.GL_FOG_MODE, fogMode[ fogFilter ] );
        if( isLighting ) {

            gl.glEnable( GL2.GL_LIGHTING );
        } else gl.glDisable( GL2.GL_LIGHTING );
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -6f );
        gl.glRotatef( rotY, 1f, 0f, 0f );
        gl.glRotatef( rotX, 0f, 1f, 0f );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureID );
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

        rotX += xSpeed;
        rotY += ySpeed;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        if( height == 0 ) {

            height = 1;
        }
        float h = ( float ) width / ( float ) height;
        glu.gluPerspective( 45, h, 0.1f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch ( e.getKeyCode() ) {

            case KeyEvent.VK_LEFT: {

                xSpeed -= 0.1f;
                break;
            }

            case KeyEvent.VK_RIGHT: {

                xSpeed += 0.1f;
                break;
            }

            case KeyEvent.VK_UP: {

                ySpeed += 0.1f;
                break;
            }

            case KeyEvent.VK_DOWN: {

                ySpeed -= 0.1f;
                break;
            }

            case KeyEvent.VK_G: {

                fogFilter += 1;
                if( fogFilter > 2 ) {

                    fogFilter = 0;
                }
                break;
            }

            case KeyEvent.VK_L: {

                isLighting = !isLighting;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
