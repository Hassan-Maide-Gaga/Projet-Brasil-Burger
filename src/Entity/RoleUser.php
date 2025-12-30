<?php

namespace App\Entity;

enum RoleUser: string
{
    case GESTIONNAIRE = 'GESTIONNAIRE';
    case CLIENT = 'CLIENT';
    
    public function getLabel(): string
    {
        return match($this) {
            self::GESTIONNAIRE => 'Gestionnaire',
            self::CLIENT => 'Client',
        };
    }
}