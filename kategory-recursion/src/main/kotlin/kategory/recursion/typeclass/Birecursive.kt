package kategory

interface Birecursive<F, G> : Typeclass, Recursive<F, G>, Corecursive<F, G>