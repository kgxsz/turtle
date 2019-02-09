(ns client.views
  (:require [re-frame.core :as re-frame]
            [client.utils :as u]
            [styles.constants :as c]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.coerce :as t.coerce]
            [client.schema :as schema]
            [cljs.spec.alpha :as spec]))


(defn ticker []
  (let [!ticks (re-frame/subscribe [:ticks])]
    (fn []
      (let [ticks @!ticks
            closes (->> ticks (map :close) sort)
            instants (map :instant ticks)
            maximum-close (apply max closes)
            minimum-close (apply min closes)
            close-spread (- maximum-close minimum-close)
            maximum-instant (apply max instants)
            minimum-instant (apply min instants)
            instant-spread (- maximum-instant minimum-instant)
            normalise-instant (fn [instant]
                                (+ (:circle-radius c/plot)
                                   (/ (* (- (:width c/plot)
                                            (* 2 (:circle-radius c/plot)))
                                         (- instant minimum-instant))
                                      instant-spread)))
            normalise-close (fn [close]
                              (- (:height c/plot)
                                 (:circle-radius c/plot)
                                 (/ (* (- (:height c/plot)
                                          (* 2 (:circle-radius c/plot)))
                                       (- close minimum-close))
                                    close-spread)))
            x-axis-labels (let [n 7
                                length (:width c/plot)
                                spacing (/ length n)
                                format (memoize
                                        (comp (partial t.format/unparse (t.format/formatter "MMM do"))
                                              t.coerce/from-long))]
                            (->> (iterate (partial + spacing) (/ spacing 2))
                                 (take n)
                                 (map (partial * (/ instant-spread length)))
                                 (map (partial + minimum-instant))
                                 (map format)))]
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
             :viewBox (u/view-box (:width c/plot) (:height c/plot))
             :class (u/bem [:ticker__plot])}
            [:g
             {:class (u/bem [:ticker__plot__circles])}
             (doall
              (for [{:keys [instant close]} ticks]
                [:circle
                 {:key instant
                  :cx (normalise-instant instant)
                  :cy (normalise-close close)
                  :r (:circle-radius c/plot)}]))]
            [:g
             {:class (u/bem [:ticker__plot__lines])}
             (doall
              (for [[initial final] (partition 2 1 ticks)]
                [:line
                 {:key (:instant initial)
                  :x1 (normalise-instant (:instant initial))
                  :y1 (normalise-close (:close initial))
                  :x2 (normalise-instant (:instant final))
                  :y2 (normalise-close (:close final))}]))]]

           [:div
            {:class (u/bem [:ticker__x-axis])}
            [:div
             {:class (u/bem [:ticker__x-axis__runner])}]
            [:div
             {:class (u/bem [:ticker__x-axis__labels])}
             (doall
              (for [x-axis-label x-axis-labels]
                [:div
                 {:key x-axis-label
                  :class (u/bem [:ticker__x-axis__labels__label])}
                 [:div
                  {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-grey-medium :align-center])}
                  x-axis-label]]))]]]

          [:div
           {:class (u/bem [:ticker__section])}
           [:div
            {:class (u/bem [:ticker__y-axis])}
            [:div
             {:class (u/bem [:ticker__y-axis__runner])}]
            [:div
             {:class (u/bem [:ticker__y-axis__labels])}
             (doall
              (for [label ["500" "400" "300" "200" "100"]]
                [:div
                 {:key label
                  :class (u/bem [:ticker__y-axis__labels__label])}
                 [:div
                  {:class (u/bem [:text :font-size-xx-small :font-weight-bold :colour-grey-medium :align-center])}
                  label]]))]]]]]))))


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
  (let [!note-ids (re-frame/subscribe [:note-ids])]
    (fn []
      [:ul
       (doall
        (for [id @!note-ids]
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
  (let [!initialising-ticks? (re-frame/subscribe [:initialising-ticks?])
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
         (if (or @!initialising-ticks? #_@!initialising-notes?)
           [:div
            {:class (u/bem [:page__sections])}
            [:div
             {:class (u/bem [:page__sections__section])}
             [:div
              {:class (u/bem [:text :font-size-medium :padding-top-xx-large :align-center])}
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
