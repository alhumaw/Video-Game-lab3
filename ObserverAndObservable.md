# CSCD212 S22 Lab3 (Observer Design Pattern)

> Tip use `equalsIgnoreCase` instead of `equals`

- Listeners (Observer)
  - `Player` (`HealthBar`) and Enemy (`HelathBar`)
    - Take damage
      - `propertyName`: `"take damage"`
      - `oldValue`: does not care (I set it to be `0` as there was no damage before)
      - `newValue`: a `int` array (not `ArrayList`) of  `damage`, `x` of Source and `y` of Source
  - `GameMaster`
    - Area Attack
      - `propertyName`: `"area attack"`
      - `oldValue`: does not care (I set it to be `0` as there was no damage before)
      - `newValue`: `int` damage value
    - Location Update
      - `propertyName`: `"location"`
      - `oldValue`: a `int` array (not `ArrayList`) of  old `x` and  old `y`
      - `newValue`: a `int` array (not `ArrayList`) of  new `x` and new `y`
    - Dead Report
      - `propertyName`: `"dead"`
      - `oldValue`: does not care (I set it to be a `int` array (not `ArrayList`) of  `x` and `y`)
      - `newValue`: a `int` array (not `ArrayList`) of  `x` and `y`
    - Color Change
      - `propertyName`: `"color change"`
      - `oldValue`: old `Color `
      - `newValue`: new `Color`
- Support (Observable)
  - `GameMaster`
    - take damage (for `HealthBar` Listeners)
  - `Agent`
    - change color
    - change location
    - dead (mostly for `Agent` with `HealthBar`)
    - area attack (for `SimpleHero`)