package com.inventar.enums;

public enum Jobposition {
    none {
        @Override
        public String toString() {
            return "Не указан";
        }
    },
    rukovoditel {
        @Override
        public String toString() {
            return "Руководитель";
        }
    },
    io_rukovoditel {
        @Override
        public String toString() {
            return "И.о. руководителя";
        }
    },

    zam_rukovoditel {
        @Override
        public String toString() {
            return "Зам. руководителя";
        }
    },
    nachalnik {
        @Override
        public String toString() {
            return "Начальник";
        }
    },
    zam_nachalnik {
        @Override
        public String toString() {
            return "Зам. начальника";
        }
    },
    glavspec {
        @Override
        public String toString() {
            return "Главный-специалист";
        }
    },
    vedspec {
        @Override
        public String toString() {
            return "Ведущий-специалист";
        }
    },
    spec1cat {
        @Override
        public String toString() {
            return "Спец-1-кат.";
        }
    },
    spec2cat {
        @Override
        public String toString() {
            return "Спец-2-кат.";
        }
    },
    praktikant {
        @Override
        public String toString() {
            return "Практикант";
        }
    },
    stazher {
        @Override
        public String toString() {
            return "Стажер";
        }
    },
    fired {
        @Override
        public String toString() { return "Уволен";
        }
    }
}
