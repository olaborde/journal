package com.osselaborde.journal.ui.recyclerview;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnItemTouchListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

public abstract class ClickItemTouchListener implements OnItemTouchListener {

    private final ItemClickGestureDetector itemGestureDetector;

    public ClickItemTouchListener(RecyclerView hostView) {
        itemGestureDetector = new ItemClickGestureDetector(hostView.getContext(),
                new ItemClickGestureListener(hostView));
    }

    private boolean isAttachedToWindow(RecyclerView hostView) {
        if (Build.VERSION.SDK_INT >= 19) {
            return hostView.isAttachedToWindow();
        } else {
            return (hostView.getHandler() != null);
        }
    }

    private boolean hasAdapter(RecyclerView hostView) {
        return (hostView.getAdapter() != null);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        if (!isAttachedToWindow(recyclerView) || !hasAdapter(recyclerView)) {
            return false;
        }

        itemGestureDetector.onTouchEvent(event);
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
        // We can silently track tap and and long presses by silently
        // intercepting touch events in the host RecyclerView.
    }

    protected abstract boolean performItemClick(RecyclerView parent, View view, int position,
        long id);
    public abstract boolean performItemLongClick(RecyclerView parent, View view, int position,
        long id);

    private class ItemClickGestureDetector {
        private final ItemClickGestureListener mGestureListener;
        private final GestureDetectorCompat gestureDetectorCompat;

        public ItemClickGestureDetector(Context context, ItemClickGestureListener listener) {
            mGestureListener = listener;
            this.gestureDetectorCompat = new GestureDetectorCompat(context, listener);
        }

        //@Override
        public boolean onTouchEvent(MotionEvent event) {
            final boolean handled = gestureDetectorCompat.onTouchEvent(event);

            final int action = event.getAction() & MotionEventCompat.ACTION_MASK;
            if (action == MotionEvent.ACTION_UP) {
                mGestureListener.dispatchSingleTapUpIfNeeded(event);
            }

            return handled;
        }
    }

    private class ItemClickGestureListener extends SimpleOnGestureListener {
        private final RecyclerView hostView;
        private View targetChild;

        public ItemClickGestureListener(RecyclerView hostView) {
            this.hostView = hostView;
        }

        public void dispatchSingleTapUpIfNeeded(MotionEvent event) {
            // When the long press hook is called but the long press listener
            // returns false, the target child will be left around to be
            // handled later. In this case, we should still treat the gesture
            // as potential item click.
            if (targetChild != null) {
                onSingleTapUp(event);
            }
        }

        @Override
        public boolean onDown(MotionEvent event) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();

            targetChild = hostView.findChildViewUnder(x, y);
            return (targetChild != null);
        }

        @Override
        public void onShowPress(MotionEvent event) {
            if (targetChild != null) {
                targetChild.setPressed(true);
            }
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            boolean handled = false;

            if (targetChild != null) {
                targetChild.setPressed(false);

                final int position = hostView.getChildPosition(targetChild);
                final long id = hostView.getAdapter().getItemId(position);
                handled = performItemClick(hostView, targetChild, position, id);

                targetChild = null;
            }

            return handled;
        }

        @Override
        public boolean onScroll(MotionEvent event, MotionEvent event2, float v, float v2) {
            if (targetChild != null) {
                targetChild.setPressed(false);
                targetChild = null;

                return true;
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            if (targetChild == null) {
                return;
            }

            final int position = hostView.getChildPosition(targetChild);
            final long id = hostView.getAdapter().getItemId(position);
            final boolean handled = performItemLongClick(hostView, targetChild, position, id);

            if (handled) {
                targetChild.setPressed(false);
                targetChild = null;
            }
        }
    }
}
