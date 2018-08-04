package personal.phillip.surfaceviewtriangleanimationexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ExampleSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    final SurfaceHolder mSurfaceHolder;
    DrawingThread mThread;
    int mRed = 0;
    int mGreen = 0;
    int mBlue = 127;
    float[] mVertices = new float[6];
    int[] mColors = {
            Color.WHITE, Color.WHITE, Color.WHITE,
            Color.WHITE, Color.WHITE, Color.WHITE };
    Paint mPaint = new Paint();
    float mAngle = 0;
    float mCenterX = 0;
    float mCenterY = 0;

    public ExampleSurfaceView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mThread = new DrawingThread();
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mThread.keepRunning = true;
        mThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        mVertices[0] = width / 2;
        mVertices[1] = height / 2;
        mVertices[2] = width / 2 + width / 5;
        mVertices[3] = height / 2 + width / 5;
        mVertices[4] = width / 2;
        mVertices[5] = height / 2 + width / 5;
        mCenterX = width / 2 + width / 10;
        mCenterY = height / 2 + width / 10;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mThread.keepRunning = false;
        boolean retry = true;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException ignore) {
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRGB(mRed, mGreen, mBlue);
        canvas.rotate(mAngle, mCenterX, mCenterY);
        canvas.drawVertices(Canvas.VertexMode.TRIANGLES, 6, mVertices, 0,
                null, 0, mColors, 0, null, 0, 0, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                synchronized (mSurfaceHolder) {
                    mRed = (int) (255 * event.getX() / getWidth());
                    mGreen = (int) (255 * event.getY() / getHeight());
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    @SuppressLint("WrongCall")
    private class DrawingThread extends Thread {
        boolean keepRunning = true;

        @Override
        public void run() {
            Canvas canvas;
            while (keepRunning) {
                canvas = null;
                try {
                    canvas = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        mAngle += 1;
                        onDraw(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        mSurfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
                // Run the draw loop at 50 fps
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignore) {
                }
            }

        }
    }
}
