(ns client.views.note-timeline
  (:require [re-frame.core :as re-frame]
            [styles.constants :as c]
            [client.utils :as u]))


(defn view [{:keys [markers]}]
  [:div
   {:class (u/bem [:note-timeline]
                  [:cell :column :height-tiny :colour-white-three])}
   [:div
    {:class (u/bem [:note-timeline__markers]
                   [:cell :row :relative :height-xx-small :margin-left-xxx-large :margin-right-x-huge])}
    (doall
     (for [{:keys [note-id focused? left]} markers]
       [:div
        {:key note-id
         :class (u/bem [:note-timeline__markers__marker (when focused? :focused)]
                       [:cell :absolute :width-x-small :height-x-small :colour-black-two])
         :style {:left left}}]))]])


(defn note-timeline []
  (let [!ticks (re-frame/subscribe [:ticks])
        !notes (re-frame/subscribe [:notes])
        !hovered-note (re-frame/subscribe [:hovered-note])]
    (fn []
      (let [ticks @!ticks
            hovered-note @!hovered-note]
        [view
         {:markers (keep
                    (fn [{:keys [note-id tick] :as note}]
                      (when-let [tick-position (u/tick-position (:tick-id tick) ticks)]
                        {:note-id note-id
                         :focused? (= note hovered-note)
                         :left (:x tick-position)}))
                    @!notes)}]))))

