import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.sun.prism.impl.BufferUtil;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Field implements GLEventListener {

    private GLU glu;
    private int base;
    private IntBuffer texturesID = IntBuffer.wrap( new int[ 2 ] );
    private float cnt1;
    private float cnt2;

    private void loadGLTextures( GL2 gl ) throws IOException {

        File txFile1 = new File( "Data/Font.jpg" );
        File txFile2 = new File( "Data/Bumps.jpg" );
        List<File> files = new ArrayList<>();
        files.add( txFile1 );
        files.add( txFile2 );
        Texture t;
        TextureData txData;
        gl.glGenTextures( 2, texturesID );
        for (int i = 0; i < files.size(); i++) {

            t = TextureIO.newTexture( files.get( i ), true );
            texturesID.put( i, t.getTextureObject() );
            txData = TextureIO.newTextureData( gl.getGLProfile(), files.get( i ), true, null );
            gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( i ) );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR );
            gl.glTexParameteri( GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR );
            gl.glTexImage2D( GL2.GL_TEXTURE_2D, 0, 3, t.getWidth(), t.getHeight(), 0,
                    GL2.GL_RGB, GL2.GL_UNSIGNED_BYTE, txData.getBuffer() );
        }
    }

    private void buildFont( GL2 gl ) {

        float cx;
        float cy;
        base = gl.glGenLists( 256 );
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( 0 ) );
        for (int i = 0; i < 256; i++) {

            cx = ( float )( i % 16 )  / 16f;
            cy = ( float )( i / 16 ) / 16f;
            gl.glNewList( base + i, GL2.GL_COMPILE );
            gl.glBegin( GL2.GL_QUADS );
            gl.glTexCoord2f( cx, 1 - cy - 0.0625f );
            gl.glVertex2i( 0, 0 );
            gl.glTexCoord2f( cx + 0.0625f, 1 - cy - 0.0625f );
            gl.glVertex2i( 16, 0 );
            gl.glTexCoord2f( cx + 0.0625f , 1 - cy );
            gl.glVertex2i( 16, 16 );
            gl.glTexCoord2f( cx, 1 - cy );
            gl.glVertex2i( 0, 16 );
            gl.glEnd();
            gl.glTranslated( 10, 0, 0 );
            gl.glEndList();
        }
    }

    private void printText( GL2 gl, int x, int y, String text, int set ) {

        if( set > 1 ) {

            set = 1;
        }
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( 0 ) );
        gl.glDisable( GL2.GL_DEPTH_TEST );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glOrtho( 0f, 640, 0, 480, -1, 1 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glTranslated( x, y, 0 );
        gl.glListBase( base - 32 + ( 128 * set ) );
        ByteBuffer str = BufferUtil.newByteBuffer( text.length() );
        str.put( text.getBytes() );
        str.flip();
        gl.glCallLists( text.length(), GL2.GL_BYTE, str );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glPopMatrix();
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glPopMatrix();
        gl.glEnable( GL2.GL_DEPTH_TEST );
    }

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        try {

            loadGLTextures( gl );
        } catch ( IOException e ) {

            e.printStackTrace();
        }
        buildFont( gl );
        gl.glEnable( GL2.GL_TEXTURE_2D );
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1f );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
        glu = GLU.createGLU( gl );
        gl.glBlendFunc( GL2.GL_SRC_ALPHA, GL2.GL_ONE );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glBindTexture( GL2.GL_TEXTURE_2D, texturesID.get( 1 ) );
        gl.glTranslatef( 0f, 0f, -5f );
        gl.glRotatef( 45f, 0f, 0f, 1f );
        gl.glRotatef( cnt1 * 30f, 1f, 1f, 0f );
        gl.glDisable( GL2.GL_BLEND );
        gl.glColor3f( 1f, 1f, 1f );
        gl.glBegin( GL2.GL_QUADS );
        gl.glTexCoord2f( 0f, 0f );
        gl.glVertex2f( -1f, 1f );
        gl.glTexCoord2f( 1f, 0f );
        gl.glVertex2f( 1f, 1f );
        gl.glTexCoord2f( 1f, 1f );
        gl.glVertex2f( 1f, -1f );
        gl.glTexCoord2f( 0f, 1f );
        gl.glVertex2f( -1f, -1f );
        gl.glEnd();
        gl.glRotatef( 90f, 1f, 1f, 0f );
        gl.glBegin( GL2.GL_QUADS );
        gl.glTexCoord2f( 0f, 0f );
        gl.glVertex2f( -1f, 1f );
        gl.glTexCoord2f( 1f, 0f );
        gl.glVertex2f( 1f, 1f );
        gl.glTexCoord2f( 1f, 1f );
        gl.glVertex2f( 1f, -1f );
        gl.glTexCoord2f( 0f, 1f );
        gl.glVertex2f( -1f, -1f );
        gl.glEnd();
        gl.glEnable( GL2.GL_BLEND );
        gl.glLoadIdentity();
        gl.glColor3f( ( float ) Math.cos( cnt1 ), ( float ) Math.sin( cnt2 ), 1f - 0.5f *
                ( float ) Math.cos( cnt1 + cnt2 ) );
        printText( gl, ( int ) ( 280 + 250 * Math.cos( cnt1 ) ), ( int ) ( 235 + 200 * Math.sin( cnt2 ) ),
                "NeHe", 0 );
        gl.glColor3f( ( float ) Math.sin( cnt2 ), 1f - 0.5f * ( float ) Math.cos( cnt1 + cnt2 ),
                ( float ) Math.cos( cnt1 ) );
        printText( gl, ( int ) ( 280 + 230 * Math.cos( cnt2 ) ), ( int ) ( 235 + 200 * Math.sin( cnt1 ) ),
                "OpenGL", 1 );
        gl.glColor3f( 0f, 0f, 1f );
        printText( gl, ( int ) ( 240 + 200 * Math.cos( ( cnt2 + cnt1 ) / 5 ) ), 2, "Giuseppe D'Agata", 0 );
        gl.glColor3f( 1f, 1f, 1f );
        printText( gl, ( int ) ( 242 + 200 * Math.cos( ( cnt2 + cnt1 ) / 5 ) ), 2, "Giuseppe D'Agata", 0 );
        cnt1 += 0.01f;
        cnt2 += 0.0081f;
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
}
