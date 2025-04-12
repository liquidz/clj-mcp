(ns clj-mcp.example
  (:gen-class)
  (:require [clj-mcp.core :as mcp]))

(defn generate-uuid
  "Generate random UUID"
  []
  (str (random-uuid)))

(defn -main [& _]
  (mcp/start
    {
     :name "clj-mcp-tools example"
     :version "0.0.1"
     :tool-vars [#'generate-uuid]}))

