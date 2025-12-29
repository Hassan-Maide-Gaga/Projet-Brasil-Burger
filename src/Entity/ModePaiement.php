<?php

namespace App\Entity;

enum ModePaiement: string
{
    case OM = 'OM';
    case WAVE = 'WAVE';
}
