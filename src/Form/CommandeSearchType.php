<?php

namespace App\Form;

use App\DTO\CommandeSearchDto;
use App\Entity\EtatCommande;
use App\Entity\TypeCommande;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\EnumType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class CommandeSearchType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('nomClient', TextType::class, [
                'required' => false,
                'attr' => [
                    'placeholder' => 'Client',
                    'autocomplete' => 'off',
                    'class' => 'form-control'
                ]
            ])
            ->add('etatCmd', EnumType::class, [
                'class' => EtatCommande::class,
                'required' => false,
                'placeholder' => 'État de la commande',
                'attr' => [
                    'class' => 'form-control'
                ]
            ])
            ->add('typeCmd', EnumType::class, [
                'class' => TypeCommande::class,
                'required' => false,
                'placeholder' => 'Type de commande',
                'attr' => [
                    'class' => 'form-control'
                ]
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => CommandeSearchDto::class,
            'attr' => [
                'data-turbo' => 'false'
            ]
        ]);
    }
}
