import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

public class Field implements GLEventListener {

    private float rot;

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();

    @Override
    public void init(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f,  0.5f );
        gl.glClearDepth( 1f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    private void renderStrokeString( GL2 gl, int font, String str ) {

        float strWidth = glut.glutStrokeLength( font, str );
        gl.glTranslatef( - strWidth / 2f, 0, 0 );
        for (int i = 0; i < str.length(); i++) {

            char c = str.charAt( i );
            glut.glutStrokeCharacter( font, c );

        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();
        gl.glTranslatef( 0f, 0f, -15f );
        gl.glRotatef( rot, 1f, 0f, 0f );
        gl.glRotatef( rot * 1.5f, 0f, 1f, 0f );
        gl.glRotatef( rot * 1.4f, 0f, 0f, 1f );
        gl.glScalef( 0.01f, 0.01f, 0f );
        gl.glColor3f( ( float ) Math.cos( rot / 20f ), ( float ) Math.sin( rot / 25f ), 1f - 0.5f *
                ( float ) Math.cos( rot / 17f ) );
        renderStrokeString( gl, GLUT.STROKE_MONO_ROMAN, String.format( "NeHe - %.2f", rot / 50f ) );
        rot += 0.5f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        GL2 gl = drawable.getGL().getGL2();
        gl.glViewport( 0, 0 , width, height );
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
