import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Field implements GLEventListener {

    private int txID;
    private GLU glu;
    private GLF glf = new GLF();
    private int timesNew1;

    private float rot;

    private void loadGLTexture( GL2 gl ) {

        File txFile = new File( "Data/Textures/lights.jpg" );

        if( txFile.exists() ) {

            try {

                Texture t = TextureIO.newTexture( txFile, true );
                txID = t.getTextureObject();
                TextureData txData = TextureIO.newTextureData( gl.getGLProfile(), txFile, true, null );
                gl.glBindTexture( GL2.GL_TEXTURE_2D, txID );
                gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
                gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_NEAREST );
                glu = GLU.createGLU( gl );
                glu.gluBuild2DMipmaps( GL2.GL_TEXTURE_2D, 3, t.getWidth(), t.getHeight(), GL2.GL_RGB,
                        GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
                gl.glTexGeni( GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR );
                gl.glTexGeni( GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_OBJECT_LINEAR );
                gl.glEnable( GL2.GL_TEXTURE_GEN_S );
                gl.glEnable( GL2.GL_TEXTURE_GEN_T );
                glf.glfInit();
                timesNew1 = glf.glfLoadFont( "Data/Fonts/times_new1.glf" );
                if( timesNew1 == GLF.GLF_ERROR ) {

                    throw new RuntimeException( "Error loading font!" );
                }
                glf.glfStringCentering( true );
            } catch ( IOException e ) {

                e.printStackTrace();
            }
        }
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        loadGLTexture( gl );
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0.5f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }


    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( ( float ) ( 1.1f * Math.cos( rot / 16f ) ), ( float ) ( 0.8f * Math.sin( rot / 20f ) ), -3f );
        gl.glRotatef( rot, 1f, 0f, 0f );
        gl.glRotatef( rot * 1.2f, 0f, 1f, 0f );
        gl.glRotatef( rot * 1.4f, 0f, 0f, 1f );
        gl.glTranslatef( -0.35f, -0.35f, 0.1f );
        //gl.glBindTexture( GL2.GL_TEXTURE_2D, txID );
        gl.glScalef( 0.5f, 0.5f, 0.5f );
        //glf.glfDraw3DSolidStringF( gl, timesNew1, "N" );
        //glf.glfDrawWiredSymbol( gl, 'S' );
        //glf.glfDraw3DSolidStringF( gl, timesNew1, "Hello, World" );
        glf.glfDrawSolidSymbol( gl, 'S' );
        rot += 0.1f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();
        if ( height == 0 ) {

            height = 1;
        }
        float h = ( float ) width / ( float ) height;
        glu.gluPerspective( 45f, h, 1f, 100f );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }
}
