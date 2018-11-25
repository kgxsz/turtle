(ns turtle.views
  (:require [re-frame.core :as re-frame]
            [turtle.utils :as u]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.periodic :as t.periodic]
            [turtle.schema :as schema]
            [cljs.spec.alpha :as spec]))


(defn logo []
  [:div.logo
   [:svg
    {:xmlns "http://www.w3.org/2000/svg"
     :viewBox "0 0 64 42"}
    [:g
     [:rect
      {:class (u/bem [:logo__square :colour-grey-dark])
       :width "9"
       :height "9"
       :rx "1"
       :transform "translate(55 33)"}]
     [:rect
      {:class (u/bem [:logo__square :colour-grey-medium])
       :width "9"
       :height "9"
       :rx "1"
       :transform "translate(44 33)"}]
     [:rect
      {:class (u/bem [:logo__square :colour-grey-medium])
       :width "9"
       :height "9"
       :rx "1"
       :transform "translate(55 22)"}]
     [:rect
      {:class (u/bem [:logo__square :colour-grey-dark])
       :width "9"
       :height "9"
       :rx "1"
       :transform "translate(44 22)"}]
     [:path
      {:class (u/bem [:logo__square :colour-grey-dark])
       :d "M37,1049H19a1,1,0,0,1-1-1v-18a1,1,0,0,1,1-1H37a1,1,0,0,1,1,1v18A1,1,0,0,1,37,1049Zm-10.687-8.008h2.064l1.952,2.848a.351.351,0,0,0,.256.16h2.128c.085,0,.128-.038.128-.112a.4.4,0,0,0-.1-.208l-1.615-2.368a5.551,5.551,0,0,0-.512-.64v-.031a2.969,2.969,0,0,0,2.16-2.88,3.071,3.071,0,0,0-1.124-2.466,4.485,4.485,0,0,0-2.876-.894H24.328a.205.205,0,0,0-.192.193v9.216a.2.2,0,0,0,.192.192H26.12a.208.208,0,0,0,.193-.192v-2.815Zm2.319-1.728H26.312v-3.04h2.319c1.214,0,1.968.589,1.968,1.536C30.6,1038.688,29.846,1039.264,28.632,1039.264Z"
       :transform "translate(4 -1007)"}]
     [:path
      {:class (u/bem [:logo__square :colour-grey-medium])
       :d "M37,1049H19a1,1,0,0,1-1-1v-18a1,1,0,0,1,1-1H37a1,1,0,0,1,1,1v18A1,1,0,0,1,37,1049Zm-13.672-14.6a.205.205,0,0,0-.192.193v9.216a.2.2,0,0,0,.192.192h4.08a5.6,5.6,0,0,0,3.9-1.314,4.61,4.61,0,0,0,1.38-3.486,4.513,4.513,0,0,0-1.516-3.5,5.711,5.711,0,0,0-3.8-1.3Zm4.08,7.776h-2.1v-5.952h2.065a2.9,2.9,0,0,1,3.136,2.976A2.769,2.769,0,0,1,27.408,1042.176Z"
       :transform "translate(-18 -1007)"}]
     [:path
      {:class (u/bem [:logo__square :colour-grey-dark])
       :d "M37,1049H19a1,1,0,0,1-1-1v-18a1,1,0,0,1,1-1H37a1,1,0,0,1,1,1v18A1,1,0,0,1,37,1049Zm-9.672-14.6a.208.208,0,0,0-.192.193v9.216a.208.208,0,0,0,.192.192H29.12a.208.208,0,0,0,.192-.192v-9.216a.208.208,0,0,0-.192-.193Z"
       :transform "translate(26 -1029)"}]
     [:path
      {:class (u/bem [:logo__square :colour-grey-medium])
       :d "M37,1049H19a1,1,0,0,1-1-1v-18a1,1,0,0,1,1-1H37a1,1,0,0,1,1,1v18A1,1,0,0,1,37,1049Zm-10.687-8.008h2.064l1.952,2.848a.351.351,0,0,0,.256.16h2.128c.085,0,.128-.038.128-.112a.4.4,0,0,0-.1-.208l-1.615-2.368a5.551,5.551,0,0,0-.512-.64v-.031a2.969,2.969,0,0,0,2.16-2.88,3.071,3.071,0,0,0-1.124-2.466,4.485,4.485,0,0,0-2.876-.894H24.328a.205.205,0,0,0-.192.193v9.216a.2.2,0,0,0,.192.192H26.12a.208.208,0,0,0,.193-.192v-2.815Zm2.319-1.728H26.312v-3.04h2.319c1.214,0,1.968.589,1.968,1.536C30.6,1038.688,29.846,1039.264,28.632,1039.264Z"
       :transform "translate(4 -1029)"}]
     [:path
      {:class (u/bem [:logo__square :colour-grey-dark])
       :d "M37,1049H19a1,1,0,0,1-1-1v-18a1,1,0,0,1,1-1H37a1,1,0,0,1,1,1v18A1,1,0,0,1,37,1049Zm-9.064-14.76a4.967,4.967,0,0,0-5.3,4.96,4.711,4.711,0,0,0,1.562,3.62,5.565,5.565,0,0,0,3.734,1.34,5.326,5.326,0,0,0,3.77-1.316,4.311,4.311,0,0,0,1.286-3.164v-.8h-5.76a.271.271,0,0,0-.224.224v1.216a.271.271,0,0,0,.224.224h3.536c-.061.891-1.055,1.792-2.832,1.792a3.185,3.185,0,0,1-2.178-.806,3.2,3.2,0,0,1,2.178-5.466,2.388,2.388,0,0,1,2.592,1.68.226.226,0,0,0,.24.16h1.648c.188,0,.256-.107.256-.208a3.254,3.254,0,0,0-1.138-2.262A5.206,5.206,0,0,0,27.936,1034.24Z"
       :transform "translate(-18 -1029)"}]]]])


(defn user-details [{:keys [first-name avatar-url]}]
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


(defn calendar [{:keys [id title subtitle colour]}]
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


(defn app []
  (let [!initialising? (re-frame/subscribe [:initialising?])]
    (fn []
      [:div
       {:class (u/bem [:app])}
       [:div
        {:class (u/bem [:notification :fixed :underlayed])}
        [:div
         {:class (u/bem [:notification__title])}
         [:div
          {:class (u/bem [:icon :warning :font-size-xxx-large :colour-red-dark])}]
         [:div
          {:class (u/bem [:text :font-size-x-large :font-weight-bold :colour-red-dark :padding-left-xxx-small])}
          "ERROR"]]
        [:div
         {:class (u/bem [:notification__paragraph])}
         [:div
          {:class (u/bem [:text :font-size-medium :colour-red-dark :align-center])}
          "This application requires a larger browser window."]]]
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
            {:class (u/bem [:text :font-size-medium])}
            "Hi Kasia!"
            ])]
        [:div
         {:class (u/bem [:page__footer])}]]])))
