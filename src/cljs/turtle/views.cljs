(ns turtle.views
  (:require [re-frame.core :as re-frame]
            [turtle.utils :as u]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.periodic :as t.periodic]
            [turtle.schema :as schema]
            [cljs.spec.alpha :as spec]))


#_(defn user-details [{:keys [first-name avatar-url]}]
  [:div
   {:class (u/bem [:user-details])}
   [:img
    {:class (u/bem [:user-details__avatar])
     :alt "user-details-avatar"
     :src avatar-url}]
   [:div
    {:class (u/bem [:user-details__first-name])}
    [:span
     {:class (u/bem [:text :font-weight-bold :font-size-huge :ellipsis])}
     first-name]]
   [:div
    {:class (u/bem [:user-details__divider])}]])


#_(defn calendar [{:keys [id title subtitle colour]}]
  (let [!checked-dates (re-frame/subscribe [:checked-dates id])
        month-label-formatter (t.format/formatter "MMM")
        day-label-formatter (t.format/formatter "E")
        date-label-formatter (t.format/formatter "EEEE do 'of' MMMM, Y")
        basic-formatter (t.format/formatters :basic-date)
        make-items (memoize
                    (fn [today]
                      (for [date (t.periodic/periodic-seq
                                  (t/minus- today (t/days (+ 356 (t/day-of-week today))))
                                  (t/plus- today (t/days 1))
                                  (t/days 1))]
                        {:date (t.format/unparse basic-formatter date)
                         :label (t.format/unparse date-label-formatter date)
                         :shaded? (odd? (t/month date))})))
        make-vertical-labels (memoize
                              (fn [today]
                                (for [date (t.periodic/periodic-seq
                                            (t/minus- today (t/days (- (t/day-of-week today) 1)))
                                            (t/plus- today (t/days (- 8 (t/day-of-week today))))
                                            (t/days 1))]
                                  {:date (t.format/unparse basic-formatter date)
                                   :label (t.format/unparse day-label-formatter date)
                                   :visible? (odd? (t/day-of-week date))})))
        make-horizontal-labels (memoize
                                (fn [today]
                                  (for [date (t.periodic/periodic-seq
                                              (t/minus- today (t/days (+ 350 (t/day-of-week today))))
                                              (t/plus- today (t/days (- 8 (t/day-of-week today))))
                                              (t/weeks 1))]
                                    {:date (t.format/unparse basic-formatter date)
                                     :label (t.format/unparse month-label-formatter date)
                                     :visible? (> 8 (t/day date))})))]
    (fn []
      [:div
       {:class (u/bem [:calendar])}
       [:div
        {:class (u/bem [:calendar__header])}
        [:span
         {:class (u/bem [:text :font-size-huge :font-weight-bold :colour-black-light])}
         title]
        [:span
         {:class (u/bem [:calendar__header__separator])}
         [:span
          {:class (u/bem [:text :font-size-huge :colour-grey-dark])}
          "—"]]
        [:span
         {:class (u/bem [:text :font-size-huge :colour-grey-dark])}
         subtitle]]
       [:div
        {:class (u/bem [:calendar__body])}
        [:div
         {:class (u/bem [:calendar__items])}
         (doall
          (for [{:keys [date label shaded?]} (make-items (t/today))]
            (let [add-checked-date (fn [] (re-frame/dispatch [:add-checked-date id date]))
                  remove-checked-date (fn [] (re-frame/dispatch [:remove-checked-date id date]))
                  checked? (contains? (set @!checked-dates) date)]
              [:div
               {:key date
                :title label
                :class (u/bem [:calendar__items__item
                               (cond
                                 checked? colour
                                 shaded? :colour-grey-medium
                                 :else :colour-grey-light)])
                :on-click (if checked? remove-checked-date add-checked-date)}])))]
        [:div
         {:class (u/bem [:calendar__labels :horizontal])}
         (doall
          (for [{:keys [date label visible?]} (make-horizontal-labels (t/today))]
            [:div
             {:key date
              :class (u/bem [:calendar__label :vertical])}
             (when visible?
               [:div
                {:class (u/bem [:text :font-size-xx-small :font-weight-bold])}
                label])]))]
        [:div
         {:class (u/bem [:calendar__labels :vertical])}
         (doall
          (for [{:keys [date label visible?]} (make-vertical-labels (t/today))]
            [:div
             {:key date
              :class (u/bem [:calendar__label :horizontal])}
             (when visible?
               [:div
                {:class (u/bem [:text :font-size-xx-small :font-weight-bold])}
                label])]))]]

       [:div
        {:class (u/bem [:calendar__footer])}]])))


(defn notification
  ([type title paragraph] (notification type title paragraph {}))
  ([type title paragraph {:keys [fixed? underlayed?]}]
   (let [icon-type (case type
                     :success :checkmark-circle
                     :warning :warning
                     :failure :warning)]
     [:div
      {:class (u/bem [:notification type (when fixed? :fixed) (when underlayed? :underlayed)])}
      [:div
       {:class (u/bem [:notification__title])}
       [:div
        {:class (u/bem [:icon icon-type :font-size-xxx-large])}]
       [:div
        {:class (u/bem [:text :font-size-x-large :font-weight-bold :padding-left-xxx-small])}
        title]]
      [:div
       {:class (u/bem [:notification__paragraph])}
       [:div
        {:class (u/bem [:text :align-center])}
        paragraph]]])))


(defn app []
  (let [!initialising? (re-frame/subscribe [:initialising?])]
    (fn []
      [:div
       {:class (u/bem [:app])}
       (notification
        :failure
        "ERROR"
        "This application requires a larger browser window."
        {:fixed? true :underlayed? true})
       [:div
        {:class (u/bem [:page])}
        [:div
         {:class (u/bem [:page__header])}]
        [:div
         {:class (u/bem [:page__body])}
         (if @!initialising?
           [:div
            {:class (u/bem [:text :font-size-medium])}
            "Loading"]
           [:div
            {:class (u/bem [:text :font-size-xx-large :font-weight-bold])}
            "Hi Kasia!"])]
        [:div
         {:class (u/bem [:page__footer])}]]])))
