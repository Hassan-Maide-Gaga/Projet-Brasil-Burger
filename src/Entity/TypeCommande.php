<?php

namespace App\Entity;

enum TypeCommande: string
{
    case SUR_PLACE = 'SUR_PLACE';
    case A_RECUPERER = 'A_RECUPERER';
    case LIVRAISON = 'LIVRAISON';
    

    public function getLabel(): string
    {
        return match($this) {
            self::SUR_PLACE => 'Sur place',
            self::A_RECUPERER => 'À récupérer',
            self::LIVRAISON => 'Livraison',
        };
    }
}