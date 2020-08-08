package com.inventar.enums;

public enum Department {
    none {
        @Override
        public String toString() {
            return "Не указан";
        }
    },
    rukovodstvo {
        @Override
        public String toString() {
            return "Руководство";
        }
    },
    kkipr {
        @Override
        public String toString() {
            return "ККиПР";
        }
    },
    pio {
        @Override
        public String toString() {
            return "ПиО";
        }
    },
    zio {
        @Override
        public String toString() {
            return "ЗИО";
        }
    },
    lk {
        @Override
        public String toString() {
            return "ЛК";
        }
    },
    buon {
        @Override
        public String toString() {
            return "БУОН";
        }
    },
    cdippt {
        @Override
        public String toString() {
            return "ЦДиППТ";
        }
    },
    pto {
        @Override
        public String toString() {
            return "ПТО";
        }
    },
    so {
        @Override
        public String toString() {
            return "СО";
        }
    },
    oddisad {
        @Override
        public String toString() {
            return "ОДДиСАД";
        }
    },
    sitbadiis {
        @Override
        public String toString() {
            return "СиТБАДиИС";
        }
    },
    uo {
        @Override
        public String toString() {
            return "ЮО";
        }
    },
    sokr {
        @Override
        public String toString() {
            return "СОКР";
        }
    },
    sahr {
        @Override
        public String toString() {
            return "САХР";
        }
    },
    kis {
        @Override
        public String toString() {
            return "КиС";
        }
    },
    vodi_i_ubor {
        @Override
        public String toString() {
            return "Водители-и-уборщицы";
        }
    }
}
