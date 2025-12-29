<?php

namespace App\Entity;

enum TypeCommandeItem: string
{
    case BURGER = 'BURGER';
    case MENU = 'MENU';
    case COMPLEMENT = 'COMPLEMENT';
}
