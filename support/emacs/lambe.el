(defvar lambe-constants
    '("kind" "sig" "def" "data" "trait" "impl" "type")
)
  
(defvar lambe-keywords
    '("with" "for" "let" "where" "in" "self" "when" "is")
)
  
(defvar lambe-tab-width nil "Width of a tab for Lambe mode")
  
(defvar lambe-font-lock-defaults
  `((
     ;; stuff between double quotes
     ("\"\\.\\*\\?" . font-lock-string-face)
     ;; ("\'\\.\\?" . font-lock-string-face)
     ("(\\|)\\|{\\|}\\|->\\|:\\|\\.\\|=\\||" . font-lock-keyword-face)
     ( ,(regexp-opt lambe-keywords 'words) . font-lock-builtin-face)
     ( ,(regexp-opt lambe-constants 'words) . font-lock-constant-face)
     ))
)
  
(define-derived-mode lambe-mode fundamental-mode "LAMBE script"
  "Lambe mode is a major mode for editing Lambe files"
  (setq font-lock-defaults lambe-font-lock-defaults)
  
  (when lambe-tab-width
    (setq tab-width lambe-tab-width))
  
  (setq comment-start "--")
  (setq comment-end "")
  
  (modify-syntax-entry ?/ "< b" lambe-mode-syntax-table)
  (modify-syntax-entry ?\n "> b" lambe-mode-syntax-table)
)
  
(provide 'lambe-mode)
