import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * Created by KITS on 21.05.2017.
 */
public class Triangle implements GLEventListener {

    @Override
    public void init(GLAutoDrawable drawable) {

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

    }

    @Override
    public void display(GLAutoDrawable drawable) {

        final GL2 gl2 = drawable.getGL().getGL2();

        //рисуем основание
        gl2.glBegin( GL2.GL_LINES );
        gl2.glVertex3f( -0.5f, -0.5f, 0 );
        gl2.glVertex3f( 0.5f, -0.5f, 0 );
        gl2.glEnd();

        //рисуем левую сторону
        gl2.glBegin( GL2.GL_LINES );
        gl2.glVertex3f( -0.5f, -0.5f, 0 );
        gl2.glVertex3f( 0f, 0.5f, 0 );
        gl2.glEnd();

        //рисуем правую сторону
        gl2.glBegin( GL2.GL_LINES );
        gl2.glVertex3f( 0.5f, -0.5f, 0 );
        gl2.glVertex3f( 0f, 0.5f, 0 );
        gl2.glEnd();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
}
