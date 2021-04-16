/*
 * Copyright (c) 2021 OBiBa. All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.obiba.opal.web.gwt.app.client.ui;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasAnimation;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * A panel that displays all of its child widgets in a 'deck', where only one can be visible at a time. It is used by
 * {@link AbstractTabPanel}.
 * <p/>
 * <p>
 * Once a widget has been added to a DeckPanel, its visibility, width, and height attributes will be manipulated. When
 * the widget is removed from the DeckPanel, it will be visible, and its width and height attributes will be cleared.
 * </p>
 */
public class TabDeckPanel extends ComplexPanel implements HasAnimation, InsertPanel.ForIsWidget {
  /**
   * An {@link Animation} used to slide in the new content.
   */
  private static class SlideAnimation extends Animation {
    /**
     * The {@link Element} holding the {@link Widget} with a lower index.
     */
    private Element container1 = null;

    /**
     * The {@link Element} holding the {@link Widget} with a higher index.
     */
    private Element container2 = null;

    /**
     * A boolean indicating whether container1 is growing or shrinking.
     */
    private boolean growing = false;

    /**
     * The fixed height of a {@link AbstractTabPanel} in pixels. If the {@link AbstractTabPanel} does not have a fixed
     * height, this will be set to -1.
     */
    private int fixedWidth = -1;

    /**
     * The old {@link Widget} that is being hidden.
     */
    private Widget oldWidget = null;

    private boolean isAnimationRunning = false;

    @Override
    public void cancel() {
      isAnimationRunning = false;
      super.cancel();
    }

    /**
     * Switch to a new {@link Widget}.
     *
     * @param oldWidget the {@link Widget} to hide
     * @param newWidget the {@link Widget} to show
     * @param animate true to animate, false to switch instantly
     */
    @SuppressWarnings("PMD.NcssMethodCount")
    public void showWidget(Widget oldWidget, Widget newWidget, boolean animate) {
      isAnimationRunning = true;
      // Immediately complete previous animation
      cancel();

      // Get the container and index of the new widget
      Element newContainer = getContainer(newWidget);
      int newIndex = DOM.getChildIndex(DOM.getParent(newContainer), newContainer);

      // If we aren't showing anything, don't bother with the animation
      if(oldWidget == null) {
        UIObject.setVisible(newContainer, true);
        newWidget.setVisible(true);
        return;
      }
      this.oldWidget = oldWidget;

      // Get the container and index of the old widget
      Element oldContainer = getContainer(oldWidget);
      int oldIndex = DOM.getChildIndex(DOM.getParent(oldContainer), oldContainer);

      // Figure out whether to grow or shrink the container
      if(newIndex > oldIndex) {
        container1 = oldContainer;
        container2 = newContainer;
        growing = true;
      } else {
        container1 = newContainer;
        container2 = oldContainer;
        growing = false;
      }

      // Start the animation
      if(animate) {
        // Figure out if the deck panel has a fixed height
        com.google.gwt.dom.client.Element deckElem = container1.getParentElement();
        int deckWidth = deckElem.getOffsetWidth();
        if(growing) {
          fixedWidth = container1.getOffsetWidth();
          container1.getStyle().setPropertyPx("width", Math.max(1, fixedWidth - 1));
        } else {
          fixedWidth = container2.getOffsetWidth();
          container2.getStyle().setPropertyPx("width", Math.max(1, fixedWidth - 1));
        }
        if(deckElem.getOffsetWidth() != deckWidth) {
          fixedWidth = -1;
        }

        // Only scope to the deck if it's fixed height, otherwise it can affect
        // the rest of the page, even if it's not visible to the user.
        run(ANIMATION_DURATION, fixedWidth == -1 ? null : deckElem);
      } else {
        onInstantaneousRun();
      }

      // We call newWidget.setVisible(true) immediately after showing the
      // widget's container so users can delay render their widget. Ultimately,
      // we should have a better way of handling this, but we need to call
      // setVisible for legacy support.
      newWidget.setVisible(true);
    }

    @Override
    protected void onComplete() {
      DOM.setStyleAttribute(container1, "width", "100%");
      DOM.setStyleAttribute(container1, "marginLeft", "0");
      DOM.setStyleAttribute(container1, "overflow", "visible");

      UIObject.setVisible(container1, !growing);
      UIObject.setVisible(container2, growing);

      DOM.setStyleAttribute(container2, "width", "100%");
      DOM.setStyleAttribute(container2, "marginLeft", "0");
      DOM.setStyleAttribute(container2, "overflow", "visible");

      container1 = null;
      container2 = null;
      hideOldWidget();
      isAnimationRunning = false;
    }

    @Override
    protected void onStart() {
      // Start the animation
      DOM.setStyleAttribute(container1, "overflow", "hidden");
      DOM.setStyleAttribute(container2, "overflow", "hidden");
      onUpdate(0.0);
      UIObject.setVisible(container1, growing);
      UIObject.setVisible(container2, !growing);
    }

    @Override
    protected void onUpdate(double progress) {
      // cut progress in two: first move out container1 and then move in container2

      if(progress < 0.5) {
        moveOut(progress, growing ? container1 : container2);
      } else {
        UIObject.setVisible(container1, !growing);
        UIObject.setVisible(container2, growing);
        moveIn(progress, growing ? container2 : container1);
      }

    }

    private void moveOut(double progress, Element container) {
      int width;
      width = fixedWidth == -1
          ? (int) (2 * progress * DOM.getElementPropertyInt(container, "scrollWidth"))
          : (int) (2 * progress * fixedWidth);
      String sign = growing ? "-" : "+";
      DOM.setStyleAttribute(container, "marginLeft", sign + normalizeWidth(width) + "px");
    }

    private void moveIn(double progress, Element container) {
      int width;
      width = fixedWidth == -1
          ? (int) ((1.5 - progress) * DOM.getElementPropertyInt(container, "scrollWidth"))
          : fixedWidth - (int) (progress * fixedWidth);
      String sign = growing ? "+" : "-";
      DOM.setStyleAttribute(container, "marginLeft", sign + normalizeWidth(width) + "px");
    }

    private int normalizeWidth(int width) {
      // Issue 2339: If the width is 0px, IE7 will display the entire content
      // widget instead of hiding it completely.
      return width == 0 ? 1 : Math.max(1, width - 1);
    }

    /**
     * Hide the old widget when the animation completes.
     */
    private void hideOldWidget() {
      // Issue 2510: Hiding the widget isn't necessary because we hide its
      // wrapper, but its in here for legacy support.
      oldWidget.setVisible(false);
      oldWidget = null;
    }

    private void onInstantaneousRun() {
      UIObject.setVisible(container1, !growing);
      UIObject.setVisible(container2, growing);
      container1 = null;
      container2 = null;
      hideOldWidget();
      isAnimationRunning = false;
    }
  }

  /**
   * The duration of the animation.
   */
  public static final int ANIMATION_DURATION = 350;

  /**
   * The {@link Animation} used to slide in the new {@link Widget}.
   */
  private static SlideAnimation slideAnimation;

  /**
   * The the container {@link Element} around a {@link Widget}.
   *
   * @param w the {@link Widget}
   * @return the container {@link Element}
   */
  private static Element getContainer(Widget w) {
    return DOM.getParent(w.getElement());
  }

  private boolean isAnimationEnabled = false;

  private Widget visibleWidget;

  /**
   * Creates an empty deck panel.
   */
  TabDeckPanel() {
    setElement(DOM.createDiv());
  }

  @Override
  public void add(Widget w) {
    Element container = createWidgetContainer();
    DOM.appendChild(getElement(), container);

    // The order of these methods is very important. In order to preserve
    // backward compatibility, the offsetWidth and offsetHeight of the child
    // widget should be defined (greater than zero) when w.onLoad() is called.
    // As a result, we first initialize the container with a height of 0px, then
    // we attach the child widget to the container. See Issue 2321 for more
    // details.
    add(w, container);

    // After w.onLoad is called, it is safe to make the container invisible and
    // set the height of the container and widget to 100%.
    finishWidgetInitialization(container, w);
  }

  /**
   * Gets the index of the currently-visible widget.
   *
   * @return the visible widget's index
   */
  public int getVisibleWidget() {
    return getWidgetIndex(visibleWidget);
  }

  @Override
  public void insert(IsWidget w, int beforeIndex) {
    insert(asWidgetOrNull(w), beforeIndex);
  }

  @Override
  public void insert(Widget w, int beforeIndex) {
    Element container = createWidgetContainer();
    DOM.insertChild(getElement(), container, beforeIndex);

    // See add(Widget) for important comments
    insert(w, container, beforeIndex, true);
    finishWidgetInitialization(container, w);
  }

  @Override
  public boolean isAnimationEnabled() {
    return isAnimationEnabled;
  }

  public boolean isAnimationRunning() {
    return slideAnimation != null && slideAnimation.isAnimationRunning;
  }

  @Override
  public boolean remove(Widget w) {
    Element container = getContainer(w);
    boolean removed = super.remove(w);
    if(removed) {
      resetChildWidget(w);

      DOM.removeChild(getElement(), container);
      if(visibleWidget == w) {
        visibleWidget = null;
      }
    }
    return removed;
  }

  @Override
  public void setAnimationEnabled(boolean enable) {
    isAnimationEnabled = enable;
  }

  /**
   * Shows the widget at the specified index. This causes the currently- visible widget to be hidden.
   *
   * @param index the index of the widget to be shown
   */
  public void showWidget(int index) {
    checkIndexBoundsForAccess(index);
    Widget oldWidget = visibleWidget;
    visibleWidget = getWidget(index);

    if(visibleWidget != oldWidget) {
      if(slideAnimation == null) {
        slideAnimation = new SlideAnimation();
      }
      slideAnimation.showWidget(oldWidget, visibleWidget, isAnimationEnabled && isAttached());
    }
  }

  /**
   * Setup the container around the widget.
   */
  private Element createWidgetContainer() {
    Element container = DOM.createDiv();
    DOM.setStyleAttribute(container, "width", "100%");
    DOM.setStyleAttribute(container, "height", "0px");
    DOM.setStyleAttribute(container, "padding", "0px");
    DOM.setStyleAttribute(container, "margin", "0px");
    return container;
  }

  /**
   * Setup the container around the widget.
   */
  private void finishWidgetInitialization(Element container, Widget w) {
    UIObject.setVisible(container, false);
    DOM.setStyleAttribute(container, "height", "100%");

    // Set 100% by default.
    Element element = w.getElement();
    if("".equals(DOM.getStyleAttribute(element, "width"))) {
      w.setWidth("100%");
    }
    if("".equals(DOM.getStyleAttribute(element, "height"))) {
      w.setHeight("100%");
    }

    // Issue 2510: Hiding the widget isn't necessary because we hide its
    // wrapper, but it's in here for legacy support.
    w.setVisible(false);
  }

  /**
   * Reset the dimensions of the widget when it is removed.
   */
  private void resetChildWidget(Widget w) {
    w.setSize("", "");
    w.setVisible(true);
  }
}
