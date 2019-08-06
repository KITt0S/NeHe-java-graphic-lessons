import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;

/**
 * Created by KITS on 22.05.2017.
 */
public class MovingTriangle implements GLEventListener {

    private float rTri;
    private GLU glu = new GLU();

    @Override
    public void init(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();

        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();

        gl.glTranslatef(0,0,-2.0f);

        gl.glScalef( 0.5f, 0.5f, 0.5f );

        gl.glRotatef( rTri, 1, 1, 0 );

        //красное основание пирамиды
        gl.glBegin( GL2.GL_POLYGON );

        //левый передний угол
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex3f( -0.5f, -0.5f, 0.5f );
        //левый задний угол
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex3f( -0.5f, -0.5f, -0.5f );
        //правый задний угол
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex3f( 0.5f, -0.5f, -0.5f );
        //правый передний угол
        gl.glColor3f( 1, 0, 0 );
        gl.glVertex3f( 0.5f, -0.5f, 0.5f );

        gl.glEnd();

        //зелёная левая грань пирамиды
        gl.glBegin( GL2.GL_TRIANGLES );

        //левый передний угол
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex3f( -0.5f, -0.5f, 0.5f );
        //левый задний угол
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex3f( -0.5f, -0.5f, -0.5f );
        //вершина
        gl.glColor3f( 0, 1, 0 );
        gl.glVertex3f( 0, 0.5f, 0 );

        gl.glEnd();

        // синяя задняя грань пирамиды
        gl.glBegin( GL2.GL_TRIANGLES );

        //левый задний угол
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex3f( -0.5f, -0.5f, -0.5f );
        //вершина
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex3f( 0, 0.5f, 0 );
        //правый задний угол
        gl.glColor3f( 0, 0, 1 );
        gl.glVertex3f( 0.5f, -0.5f, -0.5f );

        gl.glEnd();

        //жёлтая правая грань пирамиды
        gl.glBegin( GL2.GL_TRIANGLES );

        //левый передний угол
        gl.glColor3f( 1, 1, 0 );
        gl.glVertex3f( -0.5f, -0.5f, 0.5f );
        //вершина
        gl.glColor3f( 1, 1, 0 );
        gl.glVertex3f( 0, 0.5f, 0 );
        //правый передний угол
        gl.glColor3f( 1, 1, 0 );
        gl.glVertex3f( 0.5f, -0.5f, 0.5f );

        gl.glEnd();

        //фиолетовая передняя грань пирамиды
        gl.glBegin( GL2.GL_TRIANGLES );

        //правый задний угол
        gl.glColor3f( 1, 0, 1 );
        gl.glVertex3f( 0.5f, -0.5f, -0.5f );
        //вершина
        gl.glColor3f( 1, 0, 1 );
        gl.glVertex3f( 0, 0.5f, 0 );
        //правый передний угол
        gl.glColor3f( 1, 0, 1 );
        gl.glVertex3f( 0.5f, -0.5f, 0.5f );

        gl.glEnd();

        gl.glFlush();

        rTri += 0.2f;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        final GL2 gl = drawable.getGL().getGL2();
        if( height == 0 )
        height = 1;

        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();
    }
}
