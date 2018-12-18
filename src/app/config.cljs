
(ns app.config )

(def cdn?
  (cond
    (exists? js/window) false
    (exists? js/process) (= "true" js/process.env.cdn)
    :else false))

(def dev?
  (let [debug? (do ^boolean js/goog.DEBUG)]
    (if debug?
      (cond
        (exists? js/window) true
        (exists? js/process) (not= "true" js/process.env.release)
        :else true)
      false)))

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
   :theme "black",
   :storage-key "cirru-snippets",
   :storage-file "storage.edn"})
