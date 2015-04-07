package com.simjessimsol.simcv.menu_main;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.simjessimsol.simcv.R;
import com.squareup.picasso.Picasso;

public class MainMenuAdapter extends RecyclerView.Adapter<ItemViewHolder> {
    private static final float EXPAND_DECELERATION = 1f;
    private static final float COLLAPSE_DECELERATION = 0.7f;
    private static final int EXPAND_DURATION = 300;
    private static final int COLLAPSE_DURATION = 250;
    private static final int ROTATE_180_DEGREE = 180;
    private static final float LIST_ELEVATION = 8f;

    private String[] dataSet;
    private Context context;
    private OnItemClickListener itemClickListener;

    private ItemViewHolder expandedView;
    private final int collapseExpandHeight;
    private int expandedPos;

    public interface OnItemClickListener {
        public void onItemClick(View v, int pos, int scale);
    }

    public MainMenuAdapter(Context context, String[] dataSet, OnItemClickListener itemClickListener) {
        this.context = context;
        this.dataSet = dataSet;
        this.itemClickListener = itemClickListener;

        Resources res = this.context.getResources();
        collapseExpandHeight = (int) res.getDimension(R.dimen.collapse_expand_height);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_menu, null);
        ItemViewHolder vh = new ItemViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemViewHolder.switchAlternative.setClickable(false);
        }

        switch (position) {
            case 0:
                Picasso.with(context).load(R.drawable.thumb_facedetect)
                        .into(itemViewHolder.thumbnail);
                itemViewHolder.title.setText(dataSet[position]);
                itemViewHolder.switchAlternative.setText("Native Camera");
                break;
            case 1:
                Picasso.with(context).load(R.drawable.thumb_circledetect)
                        .into(itemViewHolder.thumbnail);
                itemViewHolder.title.setText(dataSet[position]);
                itemViewHolder.switchAlternative.setVisibility(View.GONE);
                break;
            case 2:
                Picasso.with(context).load(R.drawable.thumb_foregrounddetect)
                        .into(itemViewHolder.thumbnail);
                itemViewHolder.title.setText(dataSet[position]);
                itemViewHolder.switchAlternative.setVisibility(View.GONE);
                break;
            case 3:
                Picasso.with(context).load(R.drawable.thumb_colortrack)
                        .into(itemViewHolder.thumbnail);
                itemViewHolder.title.setText(dataSet[position]);
                itemViewHolder.switchAlternative.setVisibility(View.GONE);
                break;
            case 4:
                itemViewHolder.title.setText(dataSet[position]);
                itemViewHolder.switchAlternative.setVisibility(View.GONE);
                itemViewHolder.seekScale.setVisibility(View.GONE);
            default:
                Picasso.with(context).load(R.drawable.ic_launcher)
                        .into(itemViewHolder.thumbnail);
                break;
        }

        itemViewHolder.collapse_expand.setClickable(true);
        itemViewHolder.collapse_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expandedPos == position) {
                    collapseItem(itemViewHolder);
                } else {
                    expandItem(itemViewHolder, position);
                }
            }
        });

        Picasso.with(context).load(R.drawable.ic_fab_play)
                .into(itemViewHolder.btnStart);
        itemViewHolder.btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, position, itemViewHolder.seekScale.getProgress() + 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.length;
    }

    private void expandItem(final ItemViewHolder itemViewHolder, int pos) {

        if (expandedView != null && expandedView != itemViewHolder) {
            collapseItem(expandedView);
        }

        expandedView = itemViewHolder;
        expandedPos = pos;

        final int startingHeight = itemViewHolder.listItem.getHeight();

        setListItemBackgroundAndElevation(itemViewHolder.listItem, true);
        setImageButtonBackgroundColor(itemViewHolder.btnStart, true);
        itemViewHolder.expandArea.setVisibility(View.VISIBLE);

        final ViewTreeObserver observer = itemViewHolder.listItem.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                final int endingHeight = itemViewHolder.listItem.getHeight();
                final int distance = endingHeight - startingHeight;
                final int collapseHeight = itemViewHolder.collapseExpandArea.getHeight();

                itemViewHolder.listItem.getLayoutParams().height = startingHeight;

                FrameLayout.LayoutParams expandParams = (FrameLayout.LayoutParams)
                        itemViewHolder.expandArea.getLayoutParams();
                expandParams.setMargins(0, -distance, 0, collapseHeight);
                itemViewHolder.listItem.requestLayout();

                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(EXPAND_DURATION);
                animator.setInterpolator(new DecelerateInterpolator(EXPAND_DECELERATION));
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();

                        itemViewHolder.listItem.getLayoutParams().height =
                                (int) (value * distance + startingHeight);
                        FrameLayout.LayoutParams expandParams = (FrameLayout.LayoutParams)
                                itemViewHolder.expandArea.getLayoutParams();
                        expandParams.setMargins(
                                0, (int) -((1 - value) * distance), 0, collapseHeight);
                        itemViewHolder.arrow.setRotation(ROTATE_180_DEGREE * value);
                        itemViewHolder.hairLine.setAlpha(1 - value);

                        itemViewHolder.listItem.requestLayout();
                    }
                });
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        itemViewHolder.listItem.getLayoutParams().height =
                                ViewGroup.LayoutParams.WRAP_CONTENT;
                        itemViewHolder.listItem.getLayoutParams().width =
                                ViewGroup.LayoutParams.MATCH_PARENT;
                        itemViewHolder.arrow.setRotation(ROTATE_180_DEGREE);
                        itemViewHolder.hairLine.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                animator.start();

                return false;
            }
        });
    }

    private void collapseItem(final ItemViewHolder itemViewHolder) {
        expandedView = null;
        expandedPos = 999;

        final int startingHeight = itemViewHolder.listItem.getHeight();

        setListItemBackgroundAndElevation(itemViewHolder.listItem, false);
        setImageButtonBackgroundColor(itemViewHolder.btnStart, false);
        itemViewHolder.expandArea.setVisibility(View.GONE);

        final ViewTreeObserver observer = itemViewHolder.listItem.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }

                final int endingHeight = itemViewHolder.listItem.getHeight();
                final int distance = endingHeight - startingHeight;

                itemViewHolder.expandArea.setVisibility(View.VISIBLE);
                itemViewHolder.hairLine.setVisibility(View.VISIBLE);

                ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f).setDuration(COLLAPSE_DURATION);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Float value = (Float) animation.getAnimatedValue();

                        itemViewHolder.listItem.getLayoutParams().height =
                                (int) (value * distance + startingHeight);
                        FrameLayout.LayoutParams expandParams = (FrameLayout.LayoutParams)
                                itemViewHolder.expandArea.getLayoutParams();
                        expandParams.setMargins(
                                0, (int) (value * distance), 0, collapseExpandHeight);
                        itemViewHolder.arrow.setRotation(ROTATE_180_DEGREE * (1 - value));
                        itemViewHolder.hairLine.setAlpha(value);

                        itemViewHolder.listItem.requestLayout();
                    }
                });
                animator.setInterpolator(new DecelerateInterpolator(COLLAPSE_DECELERATION));
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        itemViewHolder.listItem.getLayoutParams().height =
                                ViewGroup.LayoutParams.WRAP_CONTENT;
                        FrameLayout.LayoutParams expandParams = (FrameLayout.LayoutParams)
                                itemViewHolder.expandArea.getLayoutParams();
                        expandParams.setMargins(0, 0, 0, collapseExpandHeight);
                        itemViewHolder.expandArea.setVisibility(View.GONE);
                        itemViewHolder.arrow.setRotation(0);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
                animator.start();

                return false;
            }
        });
    }

    private void setListItemBackgroundAndElevation(LinearLayout layout, boolean expanded) {
        if (expanded) {
            layout.setBackgroundResource(R.color.colorPrimaryLight);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                layout.setElevation(LIST_ELEVATION);
            }
        } else {
            layout.setBackgroundResource(R.color.colorPrimary);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                layout.setElevation(0);
            }
        }
    }

    private void setImageButtonBackgroundColor(ImageButton imgButton, boolean expanded) {
        if (expanded) {
            imgButton.setBackgroundResource(R.color.colorPrimaryLight);
        } else {
            imgButton.setBackgroundResource(R.color.colorPrimary);
        }
    }
}
