import com.jogamp.opengl.GL;
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
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Field implements GLEventListener, KeyListener {

    private float roll;
    private boolean masking = true;
    private boolean scene;
    private GLU glu;
    private IntBuffer textureIDs = IntBuffer.wrap( new int[ 5 ] );


    private void loadGLTextures( GL2 gl ) throws IOException {

        List<File> txFiles = new ArrayList<>();
        txFiles.add( new File( "Data/logo.png" ) );
        txFiles.add( new File( "Data/mask1.png" ) );
        txFiles.add( new File( "Data/image1.png" ) );
        txFiles.add( new File( "Data/mask2.png" ) );
        txFiles.add( new File( "Data/image2.png" ) );
        gl.glGenTextures( 5, textureIDs );
        for (int i = 0; i < 5; i++) {

            Texture texture = TextureIO.newTexture( txFiles.get( i ), true );
            textureIDs.put( texture.getTextureObject() );
            TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txFiles.get( i ), true, null );
            gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( i ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, texture.getWidth(), texture.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        }
    }

    @Override
    public void init( GLAutoDrawable drawable ) {

        GL2 gl = drawable.getGL().getGL2();
        try {

            loadGLTextures( gl );
        } catch ( IOException e ) {

            e.printStackTrace();
        }
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        //gl.glDepthFunc( GL2.GL_LEQUAL );
        //gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        glu = GLU.createGLU( gl );
    }

    @Override
    public void dispose( GLAutoDrawable drawable ) {


    }

    @Override
    public void display( GLAutoDrawable drawable ) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -2f );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 0 ) );
        gl.glBegin( GL2.GL_QUADS );
        gl.glTexCoord2f( 0f, -roll + 0f );
        gl.glVertex3f( -1.1f, -1.1f, 0f );
        gl.glTexCoord2f( 3f, -roll + 0f );
        gl.glVertex3f( 1.1f, -1.1f, 0f );
        gl.glTexCoord2f( 3f, -roll + 3f );
        gl.glVertex3f( 1.1f, 1.1f, 0f );
        gl.glTexCoord2f( 0f, -roll + 3f );
        gl.glVertex3f( -1.1f, 1.1f, 0f );
        gl.glEnd();
        gl.glEnable( GL2.GL_BLEND );
        gl.glDisable( GL2.GL_DEPTH_TEST );
        if( masking ) {

            gl.glBlendFunc( GL2.GL_DST_COLOR, GL2.GL_ZERO );
        }
        if( scene ) {

            gl.glTranslatef( 0f, 0f, -1f );
            gl.glRotatef( roll * 360f, 0f, 0f, 1f );
            if( masking ) {

                gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 3 ) );
                gl.glBegin( GL2.GL_QUADS );
                gl.glTexCoord2f( 0f, 0f ); gl.glVertex3f( -1.1f, -1.1f, 0f );
                gl.glTexCoord2f( 1f, 0f ); gl.glVertex3f( 1.1f, -1.1f, 0f );
                gl.glTexCoord2f( 1f, 1f ); gl.glVertex3f( 1.1f, 1.1f, 0f );
                gl.glTexCoord2f( 0f, 1f ); gl.glVertex3f( -1.1f, 1.1f, 0f );
                gl.glEnd();
            }
            gl.glBlendFunc( GL2.GL_ONE, GL2.GL_ONE );
            gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 4 ) );
            gl.glBegin( GL2.GL_QUADS );
            gl.glTexCoord2f( 0f, 0f ); gl.glVertex3f( -1.1f, -1.1f, 0f );
            gl.glTexCoord2f( 1f, 0f ); gl.glVertex3f( 1.1f, -1.1f, 0f );
            gl.glTexCoord2f( 1f, 1f ); gl.glVertex3f( 1.1f, 1.1f, 0f );
            gl.glTexCoord2f( 0f, 1f ); gl.glVertex3f( -1.1f, 1.1f, 0f );
            gl.glEnd();
        } else {

            if( masking ) {

                gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 1 ) );
                gl.glBegin( GL2.GL_QUADS );
                gl.glTexCoord2f( roll + 0.0f, 0.0f );
                gl.glVertex3f( -1.1f, -1.1f, 0.0f );
                gl.glTexCoord2f( roll + 4.0f, 0.0f );
                gl.glVertex3f( 1.1f, -1.1f, 0.0f );
                gl.glTexCoord2f( roll + 4.0f, 4.0f );
                gl.glVertex3f( 1.1f, 1.1f, 0.0f );
                gl.glTexCoord2f( roll + 0.0f, 4.0f );
                gl.glVertex3f( -1.1f, 1.1f, 0.0f );
                gl.glEnd();
            }
            gl.glBlendFunc( GL2.GL_ONE, GL2.GL_ONE );
            gl.glBindTexture( GL2.GL_TEXTURE_2D, textureIDs.get( 2 ) );
            gl.glBegin( GL2.GL_QUADS );
            gl.glTexCoord2f( roll + 0.0f, 0.0f );
            gl.glVertex3f( -1.1f, -1.1f, 0.0f );
            gl.glTexCoord2f( roll + 4.0f, 0.0f );
            gl.glVertex3f( 1.1f, -1.1f, 0.0f );
            gl.glTexCoord2f( roll + 4.0f, 4.0f );
            gl.glVertex3f( 1.1f, 1.1f, 0.0f );
            gl.glTexCoord2f( roll + 0.0f, 4.0f );
            gl.glVertex3f( -1.1f, 1.1f, 0.0f );
            gl.glEnd();
        }
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDisable( GL2.GL_BLEND );
        roll += 0.002f;
        if( roll > 1f ) {

            roll -= 1f;
        }
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
        glu.gluPerspective( 45f, h, 0.1f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch ( e.getKeyCode() ) {

            case KeyEvent.VK_SPACE: {

                scene = !scene;
                break;
            }

            case KeyEvent.VK_M: {

                masking = !masking;
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
