--
-- Created by Dependently-Typed Lambda Calculus on 2021-03-18
-- lambe
-- Author: dplaindoux
--

{-# OPTIONS --without-K --safe #-}

module Lambe where

open import Relation.Nullary
     using (yes; no)

open import Data.List
     using (List; []; [_]; _∷_; _++_)

open import Data.String
     using (String; _≟_)

open import Relation.Binary.PropositionalEquality
     using (_≢_; refl; _≡_)

module Kind where
    {-
    κ =
    | ⋆
    | κ → κ
    | K

    K = {mi:κi}I
    -}

    data t : Set where
      *    : t
      _->_ : t → t → t
      K    : List (String*t) → t
