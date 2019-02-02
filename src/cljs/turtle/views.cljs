(ns turtle.views
  (:require [re-frame.core :as re-frame]
            [turtle.utils :as u]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.coerce :as t.coerce]
            [turtle.schema :as schema]
            [cljs.spec.alpha :as spec]))


(defn ticker []
  (let [!ticker (re-frame/subscribe [:ticker])]
    (fn []
      (let [ticker @!ticker
            closes (->> ticker (map :close) sort)
            instants (map :instant ticker)
            maximum-close (apply max closes)
            minimum-close (apply min closes)
            maximum-instant (apply max instants)
            minimum-instant (apply min instants)
            normalise-instant (fn [instant]
                                (+ 2
                                   (* 896
                                      (/ (- instant minimum-instant)
                                         (- maximum-instant minimum-instant)))))
            normalise-close (fn [close]
                              (- 298
                                 (* 296
                                    (/ (- close minimum-close)
                                       (- maximum-close minimum-close)))))]
        [:div
         {:class (u/bem [:ticker])}
         [:div
          {:class (u/bem [:ticker__body])}

          [:div
           {:class (u/bem [:ticker__section])}

           [:div
            {:class (u/bem [:ticker__title])}
            [:div
             {:class (u/bem [:text :font-size-xx-huge :font-weight-bold :colour-grey-medium])}
             "APPL"]]

           [:svg
            {:xmlns "http://www.w3.org/2000/svg"
             :viewBox "0 0 900 300"
             :class (u/bem [:ticker__plot])}
            [:g
             {:class (u/bem [:ticker__plot__circles])}
             (doall
              (for [{:keys [instant close]} ticker]
                [:circle
                 {:key instant
                  :cx (normalise-instant instant)
                  :cy (normalise-close close)
                  :r 2}]))]
            [:g
             {:class (u/bem [:ticker__plot__lines])}
             (doall
              (for [[origin terminus] (partition 2 1 ticker)]
                [:line
                 {:key (:instant origin)
                  :x1 (normalise-instant (:instant origin))
                  :y1 (normalise-close (:close origin))
                  :x2 (normalise-instant (:instant terminus))
                  :y2 (normalise-close (:close terminus))}]))]]

           [:div
            {:class (u/bem [:ticker__x-axis])}
            #_{:style {"clear" "both"
                     "width" "1024px"
                     "height" "50px"
                     "backgroundColor" "aqua"}}
            "x axis"]]

          [:div
           {:class (u/bem [:ticker__section])}
           [:div
            {:class (u/bem [:ticker__y-axis])}
            "y axis"
            #_{:style {"float" "right"
                          "width" "100px"
                          "height" "300px"
                          "backgroundColor" "green"
                          "position" "relative"}}
            #_[:div #_{:style {"backgroundColor" "black"
                           "height" "300px"
                           "width" "10px"
                           "left" "40px"
                           "position" "absolute"}}]
            #_[:div #_{:style {"backgroundColor" "green"
                           "height" "20px"
                           "width" "40px"
                           "left" "40px"
                           "top" "10px"
                           "position" "absolute"}}
             "100"]]]
          
          #_[:div maximum-close]
          #_[:div minimum-close]
          #_[:div (t.format/unparse (t.format/formatters :basic-date-time) (t.coerce/from-long maximum-instant))]
          #_[:div minimum-instant]]]))))


(defn note-adder []
  (let [!input-value (re-frame/subscribe [:input-value])
        add-note (fn [e]
                   (re-frame/dispatch [:add-note])
                   (.preventDefault e))
        update-input-value (fn [e]
                             (let [input-value (-> e .-target .-value)]
                               (re-frame/dispatch [:update-input-value input-value])))]
    (fn []
      (let [valid-input-value? (spec/valid? ::schema/text @!input-value)]
        [:form
         {:on-submit add-note}
         [:input
          {:type :text
           :value @!input-value
           :placeholder "type in here Kasia!"
           :on-change update-input-value}]
         [:input
          {:class (when-not valid-input-value? "note-adder__button--disabled")
           :type :submit
           :value "add"
           :disabled (not valid-input-value?)}]]))))



(defn note [id]
  (let [!note (re-frame/subscribe [:note id])]
    (fn []
      [:li
       (:text @!note)])))


(defn notes []
  (let [!note-list (re-frame/subscribe [:note-list])]
    (fn []
      [:ul
       (doall
        (for [id @!note-list]
          ^{:key id} [note id]))])))


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
  (let [!initialising-ticker? (re-frame/subscribe [:initialising-ticker?])
        !initialising-notes? (re-frame/subscribe [:initialising-notes?])]
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
         (if (or @!initialising-ticker? #_@!initialising-notes?)
           [:div
            {:class (u/bem [:page__sections])}
            [:div
             {:class (u/bem [:page__sections__section])}
             [:div
              {:class (u/bem [:text :font-size-medium])}
              "Loading"]]]
           [:div
            {:class (u/bem [:page__sections])}
            [:div
             {:class (u/bem [::page__sections__section])}
             [ticker]]
            #_[:div
             {:class (u/bem [:section])}
             [note-adder]]])]
        [:div
         {:class (u/bem [:page__footer])}]]])))
