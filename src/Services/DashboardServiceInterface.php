<?php
namespace App\Services;

use App\Entity\Commande;

interface DashboardServiceInterface{
    function getCommandesEnCours() : ?int;
    function getCommandesValidees() : ?int;
    function getCommandesAnnulees(): ?int;
    function getLastCommandes() : array;
    function getRecettes() : ?float;
}