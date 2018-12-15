
(ns app.config (:require [cumulo-util.core :refer [get-env!]]))

(def cdn?
  (cond
    (exists? js/window) false
    (exists? js/process) (= "true" js/process.env.cdn)
    :else false))

(def dev?
  (let [debug? (do ^boolean js/goog.DEBUG)]
    (cond
      (exists? js/window) debug?
      (exists? js/process) (not= "true" js/process.env.release)
      :else true)))

(def site
  {:port 11010,
   :title "Snippets",
   :icon "http://cdn.tiye.me/logo/cirru.png",
   :dev-ui "http://localhost:8100/main.css",
   :release-ui "http://cdn.tiye.me/favored-fonts/main.css",
   :cdn-url "http://cdn.tiye.me/calcit-snippets/",
   :cdn-folder "tiye.me:cdn/calcit-snippets",
   :upload-folder "tiye.me:repo/Cirru/calcit-snippets/",
   :server-folder "tiye.me:servers/calcit-snippets",
   :theme "#eeeeff",
   :storage-file "storage.edn"})
