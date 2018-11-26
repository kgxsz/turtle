(ns turtle.views
  (:require [re-frame.core :as re-frame]
            [turtle.utils :as u]
            [cljs-time.core :as t]
            [cljs-time.format :as t.format]
            [cljs-time.periodic :as t.periodic]
            [turtle.schema :as schema]
            [cljs.spec.alpha :as spec]))


(defn note-adder []
  (let [!input-value (re-frame/subscribe [:input-value])
        add-note (fn [e]
                   (js/console.warn "ADDING NOTE")
                   #_(let [added-at (.now js/Date)]
                     (re-frame/dispatch [:add-item-to-item-list added-at]))
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
            [note-adder]])]
        [:div
         {:class (u/bem [:page__footer])}]]])))
