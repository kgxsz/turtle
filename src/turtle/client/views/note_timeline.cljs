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
     (for [{:keys [note-id left]} markers]
       [:div
        {:key note-id
         :class (u/bem [:note-timeline__markers__marker]
                       [:cell :absolute :width-x-small :height-x-small :colour-black-two :opacity-20])
         :style {:left left}}]))]])


(defn note-timeline []
  (let [!ticks (re-frame/subscribe [:ticks])
        !notes (re-frame/subscribe [:notes])]
    (fn []
      (let [ticks @!ticks]
        [view
         {:markers (for [{:keys [note-id tick] :as note} @!notes]
                     (let [{:keys [x]} (u/tick-position (:tick-id tick) ticks)]
                       {:note-id note-id
                        :left x}))}]))))

